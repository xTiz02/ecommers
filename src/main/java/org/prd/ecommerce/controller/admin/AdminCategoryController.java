package org.prd.ecommerce.controller.admin;

import jakarta.validation.Valid;
import org.prd.ecommerce.config.util.enums.StatusType;
import org.prd.ecommerce.controller.exceptions.controll.ResourceNotFoundException;
import org.prd.ecommerce.entities.dto.ApiResponse;
import org.prd.ecommerce.entities.dto.ApiResponseBody;
import org.prd.ecommerce.entities.dto.CategoryDto;
import org.prd.ecommerce.entities.dto.PageResponse;
import org.prd.ecommerce.services.CategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminCategoryController {



    private final CategoryService categoryService;

    public AdminCategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping("/category")
    public ResponseEntity<?> createCategory(@ModelAttribute @Valid CategoryDto categoryDto) throws Exception {
        CategoryDto createdCategory = categoryService.createCategory(categoryDto);
        return new ResponseEntity<>(
                new ApiResponse(
                        (new Date()).toString(),
                        new ApiResponseBody(
                                "Category created",
                                null,
                                createdCategory,
                                StatusType.OK.name(),
                                HttpStatus.CREATED.value()),
                        "/api/admin/category"),
                HttpStatus.CREATED);
    }

    @GetMapping("/categories")
    public ResponseEntity<?> getAllCategories() {
        List<CategoryDto> categories = categoryService.getAllCategories();
        if (categories == null || categories.isEmpty()) {
            throw new ResourceNotFoundException("categories");
        }
        return new ResponseEntity<>(
                new ApiResponse(
                        (new Date()).toString(),
                        new ApiResponseBody(
                                "List of categories",
                                null,
                                categories,
                                StatusType.DATA_OK.name(),
                                HttpStatus.OK.value()),
                        "/api/admin/category"),HttpStatus.OK);
    }
    @GetMapping("/categories/pages")
    public ResponseEntity<?> getAllPruebaCategories(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "3") int size) {
        PageResponse pageCategories = categoryService.getAllCategoriesWithPagination(page, size);
        if (pageCategories.getContent() == null || pageCategories.getContent().isEmpty()) {
            throw new ResourceNotFoundException("category page");
        }
        return new ResponseEntity<>(
                new ApiResponse(
                        (new Date()).toString(),
                        new ApiResponseBody(
                                "Category page",
                                null,
                                pageCategories,
                                StatusType.DATA_OK.name(),
                                HttpStatus.OK.value()),
                        "/api/admin/categories/pages"),HttpStatus.OK);
    }

    @GetMapping("/search/categories/{title}/pages")
    public ResponseEntity<?> getAllCategoriesPageByName(@PathVariable("title") String title, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "3") int size) {
        PageResponse pageCategories = categoryService.getAllCategoriesPageByName(title, page, size);
        if (pageCategories.getContent() == null || pageCategories.getContent().isEmpty()) {
            throw new ResourceNotFoundException("category page by name");
        }
        return new ResponseEntity<>(
                new ApiResponse(
                        (new Date()).toString(),
                        new ApiResponseBody(
                                "Category page by name",
                                null,
                                pageCategories,
                                StatusType.DATA_OK.name(),
                                HttpStatus.OK.value()),
                        "/api/admin/categories/"+title+"/pages"),HttpStatus.OK);
    }
//    @GetMapping("/categories/pages/sort")
//    public ResponseEntity<?> getAllPruebaCategories(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "3") int size, @RequestParam(defaultValue = "asc") String sort, @RequestParam(defaultValue = "name") String field) {
//        PageResponse pageCategories = categoryService.getAllCategoriesWithPaginationAndSorting(page, size, sort, field);
//        if (pageCategories.getContent() == null || pageCategories.getContent().isEmpty()) {
//            throw new ResourceNotFoundException("category page");
//        }
//        return new ResponseEntity<>(
//                new ApiResponse(
//                        (new Date()).toString(),
//                        new ApiResponseBody(
//                                "Ordered category page",
//                                null,
//                                pageCategories,
//                                StatusType.DATA_OK.name(),
//                                HttpStatus.OK.value()),
//                        "/api/admin/categories/pages/sort"),HttpStatus.OK);
//    }
    @GetMapping("/category/{id}")
    public ResponseEntity<?> getCategoryById(@PathVariable("id") Long id) {
        if (!categoryService.existsCategoryById(id)) {
            throw new ResourceNotFoundException("category", "id", id.toString());
        }
        CategoryDto category = categoryService.getCategoryById(id);
        return new ResponseEntity<>(
                new ApiResponse(
                        (new Date()).toString(),
                        new ApiResponseBody(
                                "Category",
                                null,
                                category,
                                StatusType.DATA_OK.name(),
                                HttpStatus.OK.value()),
                        "/api/admin/category/"+id),HttpStatus.OK);
    }
    @PutMapping("/category/{id}")
    public ResponseEntity<?> updateCategory(@PathVariable("id") Long id, @ModelAttribute @Valid CategoryDto categoryDto) throws Exception {
        if (!categoryService.existsCategoryById(id)) {
            throw new ResourceNotFoundException("category", "id", id.toString());
        }
        CategoryDto updatedCategory = categoryService.updateCategory(id, categoryDto);
        return new ResponseEntity<>(
                new ApiResponse(
                        (new Date()).toString(),
                        new ApiResponseBody(
                                "Category updated",
                                null,
                                updatedCategory,
                                StatusType.OK.name(),
                                HttpStatus.OK.value()),
                        "/api/admin/category/"+id),HttpStatus.OK);
    }
    @DeleteMapping("/category/{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable("id") Long id) throws Exception {
        if (!categoryService.existsCategoryById(id)) {
            throw new ResourceNotFoundException("category", "id", id.toString());
        }
        categoryService.deleteCategory(id);
        return new ResponseEntity<>(
                new ApiResponse(
                        (new Date()).toString(),
                        new ApiResponseBody(
                                "Category deleted",
                                null,
                                null,
                                StatusType.OK.name(),
                                HttpStatus.OK.value()),
                        "/api/admin/category/"+id),HttpStatus.OK);
    }

}
