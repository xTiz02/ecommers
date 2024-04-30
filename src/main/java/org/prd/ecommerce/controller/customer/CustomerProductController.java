package org.prd.ecommerce.controller.customer;

import org.prd.ecommerce.config.util.enums.StatusType;
import org.prd.ecommerce.controller.exceptions.controll.ResourceNotFoundException;
import org.prd.ecommerce.entities.dto.*;
import org.prd.ecommerce.services.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/customer")
public class CustomerProductController {
    private final ProductService productService;

    public CustomerProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/products/first/{num}")
    public ResponseEntity<?> getAllProducts(@PathVariable("num") Integer num) {
        List<ProductCustomerView> products = productService.getFirstNumProductsOrderByCreatedAtDesc(num);
        if (products == null || products.isEmpty()) {
            throw new ResourceNotFoundException("products");
        }
        return new ResponseEntity<>(
                new ApiResponse(
                        (new Date()).toString(),
                        new ApiResponseBody(
                                "List of 8 products",
                                null,
                                products,
                                StatusType.DATA_OK.name(),
                                HttpStatus.OK.value()),
                        "/api/customer/products"),
                HttpStatus.OK);
    }
    @GetMapping("/products/promotions/first/{num}")
    public ResponseEntity<?> getAllProductsPromotions(@PathVariable("num") Integer num) {
        List<ProductCustomerView> products = productService.getFirstNumProductsOrderByPromotionsDiscountDesc(num);
        if (products == null || products.isEmpty()) {
            throw new ResourceNotFoundException("products");
        }
        return new ResponseEntity<>(
                new ApiResponse(
                        (new Date()).toString(),
                        new ApiResponseBody(
                                "List of 8 products with promotions",
                                null,
                                products,
                                StatusType.DATA_OK.name(),
                                HttpStatus.OK.value()),
                        "/api/customer/products/promotions/first/"+num),
                HttpStatus.OK);
    }

    @GetMapping("/product/{id}")
    public ResponseEntity<?> getProductById(@PathVariable("id") Long id) {
        if (!productService.existsProductById(id)) {
            throw new ResourceNotFoundException("product", "id", id);
        }
        ProductCustomerView product = productService.getProductCustomerView(id);

        return new ResponseEntity<>(
                new ApiResponse(
                        (new Date()).toString(),
                        new ApiResponseBody(
                                "Product",
                                null,
                                product,
                                StatusType.DATA_OK.name(),
                                HttpStatus.OK.value()),
                        "/api/customer/product/" + id),
                HttpStatus.OK);
    }

    @GetMapping("/products/page")
    public ResponseEntity<?> getAllProductsPage(@RequestParam(required = false) String title,@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "16") int size) {
        PageResponse products = productService.getSimpleProductsViewPage(title,page, size);
        System.out.println("title: "+title);
        System.out.println("page: "+page);
        System.out.println("size: "+size);
        if (products.getContent() == null || products.getContent().isEmpty()) {
            throw new ResourceNotFoundException("products customer page");
        }
        return new ResponseEntity<>(
                new ApiResponse(
                        (new Date()).toString(),
                        new ApiResponseBody(
                                "Simple page products",
                                null,
                                products,
                                StatusType.DATA_OK.name(),
                                HttpStatus.OK.value()),
                        "/api/customer/products/page"),
                HttpStatus.OK);
    }
    @PostMapping("/products/filtered/page")
    public ResponseEntity<?> getAllProductsFilteredPage(@RequestBody FilterData filterData) {
        System.out.println(filterData);
        PageResponse products = productService.getProductsByFilter(filterData);
//        System.out.println("title: "+title);
//        System.out.println("page: "+page);
//        System.out.println("size: "+size);
//        if (products.getContent() == null || products.getContent().isEmpty()) {
//            throw new ResourceNotFoundException("products customer page");
//        }
        return new ResponseEntity<>(
                new ApiResponse(
                        (new Date()).toString(),
                        new ApiResponseBody(
                                "Simple Filtered page products",
                                null,
                                products,
                                StatusType.DATA_OK.name(),
                                HttpStatus.OK.value()),
                        "/api/customer/products/filtered/page"),
                HttpStatus.OK);
    }
}
