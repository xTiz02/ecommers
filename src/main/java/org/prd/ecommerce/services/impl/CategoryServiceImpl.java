package org.prd.ecommerce.services.impl;

import org.prd.ecommerce.config.util.enums.FolderName;
import org.prd.ecommerce.config.util.enums.StatusType;
import org.prd.ecommerce.config.util.mapper.EntitiesMapper;
import org.prd.ecommerce.controller.exceptions.services.DuplicateDataException;
import org.prd.ecommerce.entities.dto.CategoryCustomerView;
import org.prd.ecommerce.entities.dto.CategoryDto;
import org.prd.ecommerce.entities.dto.PageResponse;
import org.prd.ecommerce.entities.dto.ProductDto;
import org.prd.ecommerce.entities.entity.Category;
import org.prd.ecommerce.entities.entity.Product;
import org.prd.ecommerce.repository.CategoryRepository;
import org.prd.ecommerce.repository.ProductRepository;
import org.prd.ecommerce.services.CategoryService;
import org.prd.ecommerce.services.CloudImageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final Logger logger = LoggerFactory.getLogger(CategoryServiceImpl.class);
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private EntitiesMapper entitiesMapper;
    @Autowired
    private CloudImageService cloudinaryService;




    @Override
    @Transactional
    public CategoryDto createCategory(CategoryDto categoryDto) throws Exception {

        if (categoryRepository.existsByName(categoryDto.getName())) {
            logger.error(String.format("Category with name %s already exists", categoryDto.getName()));
            throw new DuplicateDataException("name", categoryDto.getName(), StatusType.DUPLICATE_DATA);
        }
        Category category = entitiesMapper.categoryDtoToCategory(categoryDto);
        Map uploadResult = cloudinaryService.uploadImageOnFolder(categoryDto.getImg(), FolderName.CATEGORY.getFolderName());
        category.setImgUrl(uploadResult.get("url").toString());
        category.setPublicId(uploadResult.get("publicId").toString());
        Category createdCategory = categoryRepository.save(category);
        logger.info(String.format("Category with id %d created successfully", createdCategory.getId()));
        return entitiesMapper.categoryToCategoryDto(createdCategory);
    }

    @Override
    public boolean existsCategoryById(Long id) {
        return categoryRepository.existsById(id);
    }

    @Override
    @Transactional
    public void deleteCategory(Long id) throws Exception {
        //preguntar antes de borrar si tiene productos asociados
        Category category = categoryRepository.findById(id).orElse(null);
        String publicId = category.getPublicId();
        if(category!=null){
            categoryRepository.deleteById(id);
            cloudinaryService.dropImage(publicId);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public CategoryDto getCategoryById(Long id) {
        Category category = categoryRepository.findById(id).orElse(null);
        return entitiesMapper.categoryToCategoryDto(category);
    }

    @Override
    @Transactional
    public CategoryDto updateCategory(Long id, CategoryDto categoryDto) throws Exception {
       Category category = categoryRepository.findById(id).orElse(null);
       if(!categoryDto.getName().equals(category.getName())){
          if(categoryRepository.existsByName(categoryDto.getName())) {
              logger.error(String.format("Category with name %s already exists", categoryDto.getName()));
              throw new DuplicateDataException("name", categoryDto.getName(), StatusType.DUPLICATE_DATA);
          }
       }
       Category updatedCategory = entitiesMapper.categoryDtoToCategory(categoryDto);
        updatedCategory.setCreatedAt(category.getCreatedAt());
        updatedCategory.setId(category.getId());
        updatedCategory.setPublicId(category.getPublicId());
        Map uploadResult = null;
        try{
           if(categoryDto.getImg()!=null){
               uploadResult = cloudinaryService.uploadImageOnFolder(categoryDto.getImg(), FolderName.CATEGORY.getFolderName());
               updatedCategory.setImgUrl(uploadResult.get("url").toString());
               updatedCategory.setPublicId(uploadResult.get("publicId").toString());
           }

           return entitiesMapper.categoryToCategoryDto(categoryRepository.save(updatedCategory));
        }catch (IOException e){
            logger.error("Error updating category: "+e);
            if(uploadResult!=null){
                cloudinaryService.dropImage(uploadResult.get("publicId").toString());
            }
            throw new Exception("Error updating category: "+e);

        }
    }

    @Override
    public List<CategoryDto> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        return entitiesMapper.categoryListToCategoryDtoList(categories);
    }

    @Override
    public PageResponse getAllCategoriesWithPagination(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Category> categories = categoryRepository.findAll(pageable);
        return new PageResponse<CategoryDto>(
                entitiesMapper.categoryListToCategoryDtoList(categories.getContent()),
                categories.getNumber(),
                categories.getSize(),
                categories.getTotalPages(),
                categories.getTotalElements(),
                categories.isLast(),
                categories.isFirst());
    }
    @Override
    @Transactional(readOnly = true)
    public PageResponse getAllCategoriesPageByName(String name,int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Category> categories = categoryRepository.findByNameContaining(name,pageable);
        return new PageResponse<CategoryDto>(
                entitiesMapper.categoryListToCategoryDtoList(categories.getContent()),
                categories.getNumber(),
                categories.getSize(),
                categories.getTotalPages(),
                categories.getTotalElements(),
                categories.isLast(),
                categories.isFirst());
    }



    @Override
    public PageResponse getAllCategoriesWithPaginationAndSorting(int page, int size, String sort, String field) {
        Sort directionSort = sort.equalsIgnoreCase("asc")?
                Sort.by(field).ascending():
                Sort.by(field).descending();
        Pageable pageable = PageRequest.of(page, size, directionSort);
        Page<Category> categories = categoryRepository.findAll(pageable);
        return new PageResponse<>(
                entitiesMapper.categoryListToCategoryDtoList(categories.getContent()),
                page,
                size,
                categories.getTotalPages(),
                categories.getTotalElements(),
                categories.isLast(),
                categories.isFirst());

    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoryCustomerView> getAllCategoriesToCustomer() {
        List<CategoryCustomerView> list = categoryRepository.getCategoriesToCustomerView();
        list.forEach(categoryCustomerView -> {
            categoryCustomerView.setCountProducts(productRepository.countByCategory_Id(categoryCustomerView.getId()));
        });
        return list;
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoryCustomerView> getAllSimpleCategoriesToCustomer() {
        return categoryRepository.getSimpleCategoriesToCustomerView();
    }


}
