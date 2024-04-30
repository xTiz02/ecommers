package org.prd.ecommerce.services.impl;

import org.prd.ecommerce.config.util.enums.FolderName;
import org.prd.ecommerce.config.util.mapper.EntitiesMapper;
import org.prd.ecommerce.controller.exceptions.controll.ResourceNotFoundException;
import org.prd.ecommerce.entities.dto.*;
import org.prd.ecommerce.entities.entity.*;
import org.prd.ecommerce.repository.*;
import org.prd.ecommerce.services.CloudImageService;
import org.prd.ecommerce.services.ProductService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    private final CloudImageService cloudImageService;
    private final ProductUnitRepository productUnitRepository;
    private final ProductImageRepository productImageRepository;

    private final PromotionRepository promotionRepository;
    private final Logger logger = org.slf4j.LoggerFactory.getLogger(ProductServiceImpl.class);
    @Autowired
    private EntitiesMapper entitiesMapper;

    public ProductServiceImpl(ProductRepository productRepository, CategoryRepository categoryRepository, CloudImageService cloudImageService, ProductUnitRepository productUnitRepository, ProductImageRepository productImageRepository, PromotionRepository promotionRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.cloudImageService = cloudImageService;
        this.productUnitRepository = productUnitRepository;
        this.productImageRepository = productImageRepository;
        this.promotionRepository = promotionRepository;
    }

    @Override
    @Transactional
    public ProductUnitDto createProduct(ProductDto productDto) throws Exception {
        Optional<Category> categoryOptional = categoryRepository.findById(productDto.getCategoryId());
        if (productDto.getImages() != null) {
            Map<String, Object> cloudResult = cloudImageService.uploadImagesOnFolder(productDto.getImages(), FolderName.PRODUCT.getFolderName(), productDto.getNamePrincipal());
            List<ProductImage> productImageLists = (List<ProductImage>) cloudResult.get("imagesProducts");
            productDto.setPrincipalUrl(cloudResult.get("principalUrl").toString());
            try {
                Product product0 = entitiesMapper.productDtoToProduct(productDto);
                product0.setCategory(categoryOptional.get());
                Product product = productRepository.save(product0);

                ProductUnit productUnit = productUnitRepository.save(new ProductUnit(null, null, productDto.getStock(), null, false, product));

                productImageLists.forEach(productImage -> {
                    productImage.setProductUnit(productUnit);
                    productImageRepository.save(productImage);
                });
                ProductUnitDto productUnitDto = entitiesMapper.productUnitToProductUnitDto(productUnit);
                productUnitDto.setImagesReturn(productImageLists.stream().map(c -> c.getUrl()).collect(Collectors.toList()));
                return productUnitDto;
            } catch (Exception e) {
                //logger.error("Upload images but product not created: "+e);
                cloudImageService.dropImagesOnFolder((List<String>) cloudResult.get("publicIds"));
                throw new Exception("Upload images but product not created: " + e);
            }
        }else{
            throw new ResourceNotFoundException("images","null","null");
        }

        //enviar otro tipo de respuesta
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductDto> getAllProducts() {
        return entitiesMapper.productListToProductDtoList(productRepository.findAll());
    }

    @Override
    public boolean existsProductById(Long id) {
        return productRepository.existsById(id);
    }

    @Override
    @Transactional
    public ProductDto updateProduct(ProductDto dtoProduct, Long id) throws Exception {
        Product product = productRepository.findById(id).orElse(null);
        if(product.getCategory().getId() != dtoProduct.getCategoryId()){
            Category category = categoryRepository.findById(dtoProduct.getCategoryId()).orElse(null);
            product.setCategory(category);
        }
        System.out.println(1);
        product = entitiesMapper.productDtoToProductToUpdate(dtoProduct, product);
        ProductUnit productUnit = product.getProductUnits().stream().filter(p -> p.getColor() == null && p.getOther() == null).findFirst().get();
        productUnit.setStock(dtoProduct.getStock());
        Map<String,Object> cloudResult = null;
        System.out.println(2);
        try{
            if(dtoProduct.getImages()!=null){
                System.out.println(3);
                cloudResult= cloudImageService.uploadImagesOnFolder(dtoProduct.getImages(), FolderName.PRODUCT.getFolderName(), dtoProduct.getNamePrincipal());
                if(cloudResult.containsKey("principalUrl")){
                    product.setPrincipalUrl(cloudResult.get("principalUrl").toString());
                    System.out.println(4);
                }
                //agregar imagenes
                ((List<ProductImage>)cloudResult.get("imagesProducts")).forEach(productImage -> {
                    productImage.setProductUnit(productUnit);
                    productUnit.getImagesProducts().add(productImage);
                });
            }
            System.out.println(5);
            List<String> publicIds = null;
            if(dtoProduct.getImagesUrl()!=null){
                publicIds = dtoProduct.getImagesUrl().stream().map(this::getPublicId).peek(e-> System.out.println(e)).collect(Collectors.toList());
                List<String> list = publicIds;
                productUnit.getImagesProducts().removeIf(p->list.contains(p.getPublicId()));
                System.out.println(6);
            }
            product.getProductUnits().removeIf(p->p.getId().equals(productUnit.getId()));
            product.getProductUnits().add(productUnit);
            Product productUpdate = productRepository.save(product);
            System.out.println(7);
            if(publicIds!=null){
                System.out.println(8);
                cloudImageService.dropImagesOnFolder(publicIds);
            }
            System.out.println(9);
            return entitiesMapper.productToProductDto(productUpdate);
        }catch (Exception e){
            if(cloudResult!=null){
                cloudImageService.dropImagesOnFolder((List<String>) cloudResult.get("publicIds"));
            }
            return null;
        }
    }

    @Override
    public ProductDto getProductById(Long id) {
        Optional<Product> productOptional = productRepository.findById(id);
        return productOptional.map(this::getProductDtoTransform).orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductCustomerView> getFirstNumProductsOrderByCreatedAtDesc(Integer num) {
        List<Product> products = productRepository.findTop8ByOrderByCreatedAtDesc();
        List<ProductCustomerView>  list = new ArrayList<>();

        products.forEach(p->{
            PromotionDto promotion = null;
            if (!p.getPromotions().isEmpty()) {
                promotion = entitiesMapper.promotionToPromotionDto(p.getPromotions().get(0));
            }
            List<ProductUnitCustomerView> productUnitCustomerViewList = new ArrayList<>();

            productUnitCustomerViewList.add(entitiesMapper.productUnitToProductUnitView(
                    p.getProductUnits().stream().
                            filter(pu->pu.getColor()==null && pu.getOther()==null).findFirst().get()));
            ProductCustomerView productCustomerView = entitiesMapper.productToProductCustomerView(p,promotion);
            productCustomerView.setUnits(productUnitCustomerViewList);
            list.add(productCustomerView);
        });
        return  list;
    }

    @Override
    public List<ProductCustomerView> getFirstNumProductsOrderByPromotionsDiscountDesc(Integer num) {
        List<Product> products = productRepository.findTop8ByOrderByPromotionsDiscountDesc();
        List<ProductCustomerView>  list = new ArrayList<>();

        products.forEach(p->{
            PromotionDto promotion = null;
            if (!p.getPromotions().isEmpty()) {
                promotion = entitiesMapper.promotionToPromotionDto(p.getPromotions().get(0));
            }
            List<ProductUnitCustomerView> productUnitCustomerViewList = new ArrayList<>();

            productUnitCustomerViewList.add(entitiesMapper.productUnitToProductUnitView(
                    p.getProductUnits().stream().
                            filter(pu->pu.getColor()==null && pu.getOther()==null).findFirst().get()));
            ProductCustomerView productCustomerView = entitiesMapper.productToProductCustomerView(p,promotion);
            productCustomerView.setUnits(productUnitCustomerViewList);
            list.add(productCustomerView);
        });
        return  list;
    }


    @Override
    @Transactional
    public void changeProductStatus(Long id, boolean status) {
        productRepository.changeEnabledStatus(id, status);
        productUnitRepository.changeEnabledStatusByProductId(id, status);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse getPageWithSimpleProductsInformation(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ProductAdminView> products = productRepository.findSimpleProductsViewInformation(pageable);
        return new PageResponse(
                products.getContent(),
                products.getNumber(),
                products.getSize(),
                products.getTotalPages(),
                products.getTotalElements(),
                products.isLast(),
                products.isFirst());
    }

    @Override
    public PageResponse getPageWithSimpleProductsInformationByNameContaining(String name, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ProductAdminView> products = productRepository.findSimpleProductsViewInformationByNameContaining(name,pageable);
        return new PageResponse(
                products.getContent(),
                products.getNumber(),
                products.getSize(),
                products.getTotalPages(),
                products.getTotalElements(),
                products.isLast(),
                products.isFirst());
    }

    @Override
    @Transactional
    public void deleteProduct(Long id) throws Exception {
        Product product = productRepository.findById(id).orElse(null);
        List<String> publicIds = product.getProductUnits().stream().flatMap(p->p.getImagesProducts().stream()).map(p->p.getPublicId()).collect(Collectors.toList());
        productRepository.deleteById(id);
        cloudImageService.dropImagesOnFolder(publicIds);
    }

    @Override
    public PageResponse getSimpleProductsViewPage(String title, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        List<ProductCustomerView> productsView = new ArrayList<>();

        Page<Product> products = null;
        if(title!=null){
            title = title.trim();
            products = productRepository.findAllProductsWithPromotionsOrWithoutByNameContaining(title,pageable);
        }else{
            products = productRepository.findAllProductsWithPromotionsOrWithout(pageable);
        }
        products.getContent().forEach(p-> {
            PromotionDto promotion = null;
            if (!p.getPromotions().isEmpty()) {
                promotion = entitiesMapper.promotionToPromotionDto(p.getPromotions().get(0));
            }
            List<ProductUnitCustomerView> productUnitCustomerViewList = new ArrayList<>();

            productUnitCustomerViewList.add(entitiesMapper.productUnitToProductUnitView(
                    p.getProductUnits().stream().
                            filter(pu->pu.getColor()==null && pu.getOther()==null).findFirst().get()));
            ProductCustomerView productCustomerView = entitiesMapper.productToProductCustomerView(p,promotion);
            productCustomerView.setUnits(productUnitCustomerViewList);
            productsView.add(productCustomerView);
        });
        return new PageResponse(
                productsView,
                products.getNumber(),
                products.getSize(),
                products.getTotalPages(),
                products.getTotalElements(),
                products.isLast(),
                products.isFirst());
    }

//    @Override
//    @Transactional(readOnly = true)
//    public PageResponse getAllProductsPageByName(String name,int page, int size) {
//        Pageable pageable = PageRequest.of(page, size);
//        Page<Product> products = productRepository.findByNameContaining(name,pageable);
//        return new PageResponse<ProductDto>(
//                entitiesMapper.productListToProductDtoList(products.getContent()),
//                products.getNumber(),
//                products.getSize(),
//                products.getTotalPages(),
//                products.getTotalElements(),
//                products.isLast(),
//                products.isFirst());
//    }

//    @Override
//    public PageResponse getAllProductsWithPagination(int page, int size) {
//        Pageable pageable = PageRequest.of(page, size);
//        Page<Product> products = productRepository.findAll(pageable);
//        return new PageResponse<ProductDto>(
//                entitiesMapper.productListToProductDtoList(products.getContent()),
//                products.getNumber(),
//                products.getSize(),
//                products.getTotalPages(),
//                products.getTotalElements(),
//                products.isLast(),
//                products.isFirst());
//    }

//    @Override
//    public PageResponse getAllProductsWithPaginationAndSorting(int page, int size, String sort, String field) {
//        Sort directionSort = sort.equalsIgnoreCase("asc")?
//                Sort.by(field).ascending():
//                Sort.by(field).descending();
//        Pageable pageable = PageRequest.of(page, size, directionSort);
//        Page<Product> products = productRepository.findAll(pageable);
//        return new PageResponse<ProductDto>(
//                entitiesMapper.productListToProductDtoList(products.getContent()),
//                page,
//                size,
//                products.getTotalPages(),
//                products.getTotalElements(),
//                products.isLast(),
//                products.isFirst());
//
//    }

    @Override
    @Transactional(readOnly = true)
    public ProductCustomerView getProductCustomerView(Long id) {
        Product product = productRepository.findById(id).orElse(null);
        List<ProductUnitCustomerView> productUnitCustomerViewList = product.getProductUnits().stream().map(productUnit -> {
            ProductUnitCustomerView productUnitCustomerView = entitiesMapper.productUnitToProductUnitView(productUnit);
            List<String> images = productUnit.getImagesProducts().stream().map(p -> p.getUrl()).collect(Collectors.toList());
            productUnitCustomerView.setImages(images);
            return productUnitCustomerView;
        }).collect(Collectors.toList());
        Promotion promotion = product.getPromotions().stream().filter(p -> (p.isEnabled())).findFirst().orElse(null);

        return entitiesMapper.productToproductCustomerView(product, productUnitCustomerViewList,entitiesMapper.promotionToPromotionDto(promotion));
       }

    @Override
    public PageResponse getProductsByFilter(FilterData filterData) {
        Pageable pageable = PageRequest.of(filterData.getIndex(), filterData.getSize());
        Page<Product> products = null;
        List<ProductCustomerView> productsView = new ArrayList<>();
        if(filterData.getCategoriesIds()!=null && filterData.getColors()!=null){//si tiene categorias y colors

        }
        if(filterData.getCategoriesIds()==null && filterData.getColors()==null){//si no tiene categorias ni colors
            if(filterData.getMenorDe()!=null && filterData.getMayorDe()!=null){//precio entre
                if(filterData.getTitle()!=null){//si tiene title
                    products = productRepository.findAllByNameContainingAndPriceGreaterThanEqualAndPriceLessThanEqual(
                            filterData.getTitle(),filterData.getMenorDe(),filterData.getMayorDe(),pageable);
                }else {
                    products = productRepository.findAllByPriceGreaterThanEqualAndPriceLessThanEqual(
                            filterData.getMenorDe(),filterData.getMayorDe(),pageable);
                }
            }
            if(filterData.getMenorDe()!=null && filterData.getMayorDe()==null){//precio menor a
                if(filterData.getTitle()!=null){//si tiene title
                    products = productRepository.findAllByNameContainingAndPriceLessThanEqual(
                            filterData.getTitle(),filterData.getMenorDe(),pageable);
                }else {
                    products = productRepository.findAllByPriceLessThanEqual(
                            filterData.getMenorDe(),pageable);
                }
            }
            if(filterData.getMenorDe()==null && filterData.getMayorDe()!=null){//precio mayor a
                if(filterData.getTitle()!=null){//si tiene title
                    products = productRepository.findAllByNameContainingAndPriceGreaterThanEqual(
                            filterData.getTitle(),filterData.getMayorDe(),pageable);
                }else {
                    products = productRepository.findAllByPriceGreaterThanEqual(
                            filterData.getMayorDe(),pageable);
                }
            }
            if(filterData.getMenorDe()==null && filterData.getMayorDe()==null){//si no tiene precio
                if(filterData.getTitle()!=null){//si tiene title
                    products = productRepository.findAllProductsWithPromotionsOrWithoutByNameContaining(
                            filterData.getTitle(),pageable);
                }else {
                    products = productRepository.findAll(pageable);
                }
            }
        }
        if(filterData.getCategoriesIds()!=null && filterData.getColors()==null){//si solo tiene categoriaas

            if(filterData.getMenorDe()!=null && filterData.getMayorDe()!=null){//precio entre
                if(filterData.getTitle()!=null){//si tiene title
                    products = productRepository.findAllByCategoriesIdsAndNameContainingAndPriceGreaterThanEqualAndPriceLessThanEqual(
                            filterData.getCategoriesIds(),filterData.getTitle(),filterData.getMenorDe(),filterData.getMayorDe(),pageable);
                }else {
                    products = productRepository.findAllByCategoriesIdsAndPriceGreaterThanEqualAndPriceLessThanEqual(
                            filterData.getCategoriesIds(),filterData.getMenorDe(),filterData.getMayorDe(),pageable);
                }
            }
            if(filterData.getMenorDe()!=null && filterData.getMayorDe()==null){//precio menor a
                if(filterData.getTitle()!=null){//si tiene title
                    products = productRepository.findAllByCategoriesIdsAndNameContainingAndPriceLessThanEqual(
                            filterData.getCategoriesIds(),filterData.getTitle(),filterData.getMenorDe(),pageable);
                }else {
                    products = productRepository.findAllByCategoriesIdsAndPriceLessThanEqual(
                            filterData.getCategoriesIds(),filterData.getMenorDe(),pageable);
                }
            }
            if(filterData.getMenorDe()==null && filterData.getMayorDe()!=null){//precio mayor a
                if(filterData.getTitle()!=null){//si tiene title
                    products = productRepository.findAllByCategoriesIdsAndNameContainingAndPriceGreaterThanEqual(
                            filterData.getCategoriesIds(),filterData.getTitle(),filterData.getMayorDe(),pageable);
                }else {
                    products = productRepository.findAllByCategoriesIdsAndPriceGreaterThanEqual(
                            filterData.getCategoriesIds(),filterData.getMayorDe(),pageable);
                }
            }
            if(filterData.getMenorDe()==null && filterData.getMayorDe()==null){//si no tiene precio
                if(filterData.getTitle()!=null){//si tiene title
                    products = productRepository.findAllByCategoriesIdsAndNameContaining(filterData.getCategoriesIds(),filterData.getTitle(),pageable);
                }else {
                    products = productRepository.findAllByCategoriesIds(filterData.getCategoriesIds(),pageable);
                }

            }
        }
        if(filterData.getCategoriesIds()==null && filterData.getColors()!=null){//si tiene solo colores

        }
        products.getContent().forEach(p-> {
            PromotionDto promotion = null;
            if (!p.getPromotions().isEmpty()) {
                promotion = entitiesMapper.promotionToPromotionDto(p.getPromotions().get(0));
            }
            List<ProductUnitCustomerView> productUnitCustomerViewList = new ArrayList<>();

            productUnitCustomerViewList.add(entitiesMapper.productUnitToProductUnitView(
                    p.getProductUnits().stream().
                            filter(pu->pu.getColor()==null && pu.getOther()==null).findFirst().get()));
            ProductCustomerView productCustomerView = entitiesMapper.productToProductCustomerView(p,promotion);
            productCustomerView.setUnits(productUnitCustomerViewList);
            productsView.add(productCustomerView);
        });
        return new PageResponse(
                productsView,
                products.getNumber(),
                products.getSize(),
                products.getTotalPages(),
                products.getTotalElements(),
                products.isLast(),
                products.isFirst());
    }

    public ProductDto getProductDtoTransform(Product product) {
        ProductDto productDto = entitiesMapper.productToProductDto(product);
        ProductUnit productUnit = product.getProductUnits().stream().filter(p -> p.getColor() == null && p.getOther() == null).findFirst().get();
        List<String> images = productUnit.getImagesProducts().stream().map(p -> p.getUrl()).collect(Collectors.toList());
        productDto.setImagesUrl(images);
        productDto.setStock(productUnit.getStock());
        return productDto;
    }
    public String getPublicId(String url) {
        String id = url.substring(url.lastIndexOf("/") + 1, url.lastIndexOf("."));
        return "products/" + id;
    }
}
