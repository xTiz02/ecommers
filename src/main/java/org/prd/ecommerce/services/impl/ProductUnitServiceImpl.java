package org.prd.ecommerce.services.impl;

import org.prd.ecommerce.config.util.enums.FolderName;
import org.prd.ecommerce.config.util.mapper.EntitiesMapper;
import org.prd.ecommerce.entities.dto.ProductUnitDto;
import org.prd.ecommerce.entities.entity.Product;
import org.prd.ecommerce.entities.entity.ProductImage;
import org.prd.ecommerce.entities.entity.ProductUnit;
import org.prd.ecommerce.repository.CategoryRepository;
import org.prd.ecommerce.repository.ProductImageRepository;
import org.prd.ecommerce.repository.ProductRepository;
import org.prd.ecommerce.repository.ProductUnitRepository;
import org.prd.ecommerce.services.CloudImageService;
import org.prd.ecommerce.services.ProductUnitService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductUnitServiceImpl implements ProductUnitService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    private final CloudImageService cloudImageService;
    private final ProductUnitRepository productUnitRepository;
    private final ProductImageRepository productImageRepository;
    private final Logger logger = LoggerFactory.getLogger(ProductUnitServiceImpl.class);

    @Autowired
    private EntitiesMapper entitiesMapper;

    public ProductUnitServiceImpl(ProductRepository productRepository, CategoryRepository categoryRepository, CloudImageService cloudImageService, ProductUnitRepository productUnitRepository, ProductImageRepository productImageRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.cloudImageService = cloudImageService;
        this.productUnitRepository = productUnitRepository;
        this.productImageRepository = productImageRepository;
    }

    @Override
    @Transactional
    public ProductUnitDto createProductUnit(ProductUnitDto productUnitDto,Long id) throws Exception {
        Product product = productRepository.findById(id).orElse(null);
        ProductUnit productUnit = entitiesMapper.productUnitDtoToProductUnit(productUnitDto);
        productUnit.setProduct(product);
        Map<String,Object> cloudResult = cloudImageService.uploadImagesOnFolder(productUnitDto.getImages(), FolderName.PRODUCT.getFolderName(), "");
        List<ProductImage> productImageLists = (List<ProductImage>) cloudResult.get("imagesProducts");
        try{
            ProductUnit productUnitCreated = productUnitRepository.save(productUnit);
            productImageLists.forEach(productImage -> {
                System.out.println(productImage.getUrl());
                productImage.setProductUnit(productUnitCreated);
                productImageRepository.save(productImage);
            });
            ProductUnitDto productUnitDtoCreated = entitiesMapper.productUnitToProductUnitDto(productUnitCreated);
            productUnitDtoCreated.setImagesReturn(productImageLists.stream().map(productImage -> productImage.getUrl()).collect(Collectors.toList()));
            return productUnitDtoCreated;
        }catch (Exception e){
            cloudImageService.dropImagesOnFolder((List<String>) cloudResult.get("publicIds"));
            throw new Exception("Upload images but product not created: "+e);

        }
    }

    @Override
    @Transactional
    public void changeProductUnitStatus(Long idUnit, boolean status) {
        productUnitRepository.changeEnabledStatusByProductUnitId(idUnit,status);
    }

    @Override
    @Transactional(readOnly = true)
    public Long countProductUnitByProductId(Long id) {
        return productUnitRepository.countByProductId(id);
    }

    @Override
    @Transactional(readOnly = true)
    public ProductUnitDto getProductUnitById(Long productId, Long productUnitId) {
        ProductUnit productUnit = productUnitRepository.findById(productUnitId).orElse(null);
        if ( productUnit != null) {
            ProductUnitDto productUnitDto = entitiesMapper.productUnitToProductUnitDto(productUnit);
            productUnitDto.setImagesReturn(productUnit.getImagesProducts().stream().map(productImage -> productImage.getUrl()).collect(Collectors.toList()));

            return productUnitDto;
        }
        return null;
    }

    @Override
    @Transactional
    public ProductUnitDto updateProductUnit(ProductUnitDto productUnitDto, Long id) throws Exception {
        ProductUnit productUnit0 = productUnitRepository.findById(id).orElse(null);
        ProductUnit productUnit = entitiesMapper.productUnitDtoToProductUnitToUpdate(productUnitDto,productUnit0);
        Map<String,Object> cloudResult = null;
        try{
            if(productUnitDto.getImages()!=null){
                cloudResult = cloudImageService.uploadImagesOnFolder(productUnitDto.getImages(), FolderName.PRODUCT.getFolderName(), null);
                ((List<ProductImage>)cloudResult.get("imagesProducts")).forEach(productImage -> {
                    productImage.setProductUnit(productUnit);
                    productUnit.getImagesProducts().add(productImage);
                });
            }
            List<String> publicIds = null;
            if(productUnitDto.getImagesReturn()!=null){
                publicIds = productUnitDto.getImagesReturn().stream().map(this::getPublicId).peek(e-> System.out.println(e)).collect(Collectors.toList());
                List<String> list = publicIds;
                productUnit.getImagesProducts().removeIf(p->list.contains(p.getPublicId()));
            }
            ProductUnit productUnitUpdated = productUnitRepository.save(productUnit);
            if(publicIds!=null){
                cloudImageService.dropImagesOnFolder(publicIds);
            }
            return entitiesMapper.productUnitToProductUnitDto(productUnitUpdated);
        }catch (Exception e){
            if(cloudResult!=null){
                cloudImageService.dropImagesOnFolder((List<String>) cloudResult.get("publicIds"));
            }
            //throw new Exception("Upload images but product not updated: "+e);
        }
        return null;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductUnitDto> getProductUnitByProductId(Long productId) {
        return productUnitRepository.findProductUnitByProductId(productId);
    }

    @Override
    public boolean existsProductUnit(Long id) {
        return productUnitRepository.existsById(id);
    }

    public String getPublicId(String url) {
        String id = url.substring(url.lastIndexOf("/") + 1, url.lastIndexOf("."));
        return "products/" + id;
    }
}
