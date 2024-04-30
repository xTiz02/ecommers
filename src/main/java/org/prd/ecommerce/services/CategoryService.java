package org.prd.ecommerce.services;

import org.prd.ecommerce.entities.dto.CategoryCustomerView;
import org.prd.ecommerce.entities.dto.CategoryDto;
import org.prd.ecommerce.entities.dto.PageResponse;

import java.io.IOException;
import java.util.List;

public interface CategoryService {

    CategoryDto createCategory(CategoryDto categoryDto) throws Exception;

    boolean existsCategoryById(Long id);
    void deleteCategory(Long id) throws Exception;
    CategoryDto getCategoryById(Long id);
    CategoryDto updateCategory(Long id, CategoryDto categoryDto) throws Exception;

    List<CategoryDto> getAllCategories();
    PageResponse getAllCategoriesWithPagination(int page, int size);

    PageResponse getAllCategoriesPageByName(String title,int page, int size);

    PageResponse getAllCategoriesWithPaginationAndSorting(int page, int size,String sort, String direction);

    List<CategoryCustomerView> getAllCategoriesToCustomer();

    List<CategoryCustomerView> getAllSimpleCategoriesToCustomer();
}
