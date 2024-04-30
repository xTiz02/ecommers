package org.prd.ecommerce.controller.customer;

import org.prd.ecommerce.config.util.enums.StatusType;
import org.prd.ecommerce.controller.exceptions.controll.ResourceNotFoundException;
import org.prd.ecommerce.entities.dto.ApiResponse;
import org.prd.ecommerce.entities.dto.ApiResponseBody;
import org.prd.ecommerce.entities.dto.CategoryCustomerView;
import org.prd.ecommerce.services.CategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/customer")
public class CustomerCategoryController {

    private final CategoryService categoryService;

    public CustomerCategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/categories")
    public ResponseEntity<?> getAllCategories() {
        List<CategoryCustomerView> categories = categoryService.getAllCategoriesToCustomer();
        if (categories == null || categories.isEmpty()) {
            throw new ResourceNotFoundException("categories");
        }
        return new ResponseEntity<>(
                new ApiResponse(
                        (new Date()).toString(),
                        new ApiResponseBody(
                                "Customer list of categories",
                                null,
                                categories,
                                StatusType.DATA_OK.name(),
                                HttpStatus.OK.value()),
                        "/api/customer/categories"),
                HttpStatus.OK);
    }
    @GetMapping("/simple/categories")
    public ResponseEntity<?> getSimpleCategories() {
        List<CategoryCustomerView> categories = categoryService.getAllSimpleCategoriesToCustomer();
        if (categories == null || categories.isEmpty()) {
            throw new ResourceNotFoundException("categories");
        }
        return new ResponseEntity<>(
                new ApiResponse(
                        (new Date()).toString(),
                        new ApiResponseBody(
                                "Customer list of categories",
                                null,
                                categories,
                                StatusType.DATA_OK.name(),
                                HttpStatus.OK.value()),
                        "/api/customer/categories"),
                HttpStatus.OK);
    }
}
