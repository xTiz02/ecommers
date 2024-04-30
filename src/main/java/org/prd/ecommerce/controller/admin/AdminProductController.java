package org.prd.ecommerce.controller.admin;

import jakarta.validation.Valid;
import org.prd.ecommerce.config.util.enums.StatusType;
import org.prd.ecommerce.controller.exceptions.controll.ResourceNotFoundException;
import org.prd.ecommerce.entities.dto.*;
import org.prd.ecommerce.entities.entity.Category;
import org.prd.ecommerce.entities.entity.Product;
import org.prd.ecommerce.repository.CategoryRepository;
import org.prd.ecommerce.services.CategoryService;
import org.prd.ecommerce.services.ProductService;
import org.prd.ecommerce.services.ProductUnitService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.*;

@RestController
@RequestMapping("/api/admin")
public class AdminProductController {

    private final ProductService productService;
    private final CategoryService categoryService;

    private final ProductUnitService productUnitService;

    public AdminProductController(ProductService productService, CategoryService categoryService, ProductUnitService productUnitService) {
        this.productService = productService;
        this.categoryService = categoryService;
        this.productUnitService = productUnitService;
    }

    @PostMapping("/product")
    public ResponseEntity<?> createProduct(@ModelAttribute @Valid ProductDto productDto) throws Exception {
        if(!categoryService.existsCategoryById(productDto.getCategoryId())){
            throw new ResourceNotFoundException("category","id",productDto.getCategoryId().toString());
        }
        ProductUnitDto createdProduct = productService.createProduct(productDto);
        System.out.println(productDto.getNamePrincipal());
        Arrays.stream(productDto.getImages()).forEach(image -> System.out.println(image.getOriginalFilename()));
        return new ResponseEntity<>(
                new ApiResponse(
                        (new Date()).toString(),
                        new ApiResponseBody(
                                "Product created",
                                null,
                                createdProduct,
                                StatusType.OK.name(),
                                HttpStatus.CREATED.value()),
                        "/api/admin/product"),
                HttpStatus.CREATED);

    }
    @DeleteMapping("/product/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable("id") Long id) throws Exception {
        if (!productService.existsProductById(id)) {
            throw new ResourceNotFoundException("product", "id", id.toString());
        }
        productService.deleteProduct(id);
        return new ResponseEntity<>(
                new ApiResponse(
                        (new Date()).toString(),
                        new ApiResponseBody(
                                "Product with id: " + id + " deleted",
                                null,
                                null,
                                StatusType.OK.name(),
                                HttpStatus.OK.value()),
                        "/api/admin/product/"+id),
                HttpStatus.OK);
    }

    @GetMapping("/products/pages")
    public ResponseEntity<?> getAllProductsPages(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int size) {
        PageResponse pageProducts = productService.getPageWithSimpleProductsInformation(page, size);
        if (pageProducts.getContent() == null || pageProducts.getContent().isEmpty()) {
            throw new ResourceNotFoundException("products admin page");
        }
        return new ResponseEntity<>(
                new ApiResponse(
                        (new Date()).toString(),
                        new ApiResponseBody(
                                "Products admin page",
                                null,
                                pageProducts,
                                StatusType.DATA_OK.name(),
                                HttpStatus.OK.value()),
                        "/api/admin/products/pages"),HttpStatus.OK);
    }

    @GetMapping("/search/products/{name}/pages")
    public ResponseEntity<?> getAllProductsByName(@PathVariable("name") String name,@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int size) {
        PageResponse pageProductsByName = productService.getPageWithSimpleProductsInformationByNameContaining(name,page, size);
        if (pageProductsByName.getContent() == null || pageProductsByName.getContent().isEmpty()) {
            throw new ResourceNotFoundException("product page");
        }
        return new ResponseEntity<>(
                new ApiResponse(
                        (new Date()).toString(),
                        new ApiResponseBody(
                                "List of products by name",
                                null,
                                pageProductsByName,
                                StatusType.DATA_OK.name(),
                                HttpStatus.OK.value()),
                        "/api/admin/search/products/"+name),HttpStatus.OK);
    }

    @PostMapping("/product/{ide}/unit")
    public ResponseEntity<?> createProductUnit(@PathVariable("ide") Long ide, @ModelAttribute @Valid ProductUnitDto productUnitDto) throws Exception {
        if(!productService.existsProductById(ide)){
            throw new ResourceNotFoundException("product","id",ide.toString());
        }
        ProductUnitDto createdProductUnit = productUnitService.createProductUnit(productUnitDto,ide);
        return new ResponseEntity<>(
                new ApiResponse(
                        (new Date()).toString(),
                        new ApiResponseBody(
                                "Product unit created",
                                null,
                                createdProductUnit,
                                StatusType.OK.name(),
                                HttpStatus.CREATED.value()),
                        "/api/admin/product/"+ide+"/unit"),
                HttpStatus.CREATED);
    }

    @GetMapping("/product/{id}/units/count")
    public ResponseEntity<?> getProductUnitCount(@PathVariable("id") Long id) {
        if (!productService.existsProductById(id)) {
            throw new ResourceNotFoundException("product", "id", id.toString());
        }
        Long count = productUnitService.countProductUnitByProductId(id);
        return new ResponseEntity<>(
                new ApiResponse(
                        (new Date()).toString(),
                        new ApiResponseBody(
                                "Product unit count",
                                null,
                                count,
                                StatusType.DATA_OK.name(),
                                HttpStatus.OK.value()),
                        "/api/admin/product/"+id+"/units/count"),HttpStatus.OK);
    }
    @GetMapping("/product/{id}/units")
    public ResponseEntity<?> getProductUnits(@PathVariable("id") Long id) {
        if (!productService.existsProductById(id)) {
            throw new ResourceNotFoundException("product", "id", id.toString());
        }
        List<ProductUnitDto> productUnits = productUnitService.getProductUnitByProductId(id);
        if (productUnits == null || productUnits.isEmpty()) {
            throw new ResourceNotFoundException("product units");
        }
        return new ResponseEntity<>(
                new ApiResponse(
                        (new Date()).toString(),
                        new ApiResponseBody(
                                "Product units",
                                null,
                                productUnits,
                                StatusType.DATA_OK.name(),
                                HttpStatus.OK.value()),
                        "/api/admin/product/"+id+"/units"),HttpStatus.OK);
    }
    @GetMapping("/product/{id}/unit/{idu}")
    public ResponseEntity<?> getProductUnit(@PathVariable("id") Long id, @PathVariable("idu") Long idu) {
        if (!productService.existsProductById(id)) {
            throw new ResourceNotFoundException("product", "id", id.toString());
        }
        if(!productUnitService.existsProductUnit(idu)){
            throw new ResourceNotFoundException("product unit","id",idu.toString());
        }
        ProductUnitDto productUnit = productUnitService.getProductUnitById(id,idu);
        if (productUnit == null) {
            throw new ResourceNotFoundException("product unit");
        }
        return new ResponseEntity<>(
                new ApiResponse(
                        (new Date()).toString(),
                        new ApiResponseBody(
                                "Product unit",
                                null,
                                productUnit,
                                StatusType.DATA_OK.name(),
                                HttpStatus.OK.value()),
                        "/api/admin/product/"+id+"/unit/"+idu),HttpStatus.OK);
    }
    @PutMapping("/product/{id}/unit/{idu}")
    public ResponseEntity<?> updateProductUnit(@PathVariable("id") Long id, @PathVariable("idu") Long idu, @ModelAttribute @Valid ProductUnitDto productUnitDto) throws Exception {
        if (!productService.existsProductById(id)) {
            throw new ResourceNotFoundException("product", "id", id.toString());
        }
        if(!productUnitService.existsProductUnit(idu)){
            throw new ResourceNotFoundException("product unit","id",idu.toString());
        }
        ProductUnitDto updatedProductUnit = productUnitService.updateProductUnit(productUnitDto,idu);
        return new ResponseEntity<>(
                new ApiResponse(
                        (new Date()).toString(),
                        new ApiResponseBody(
                                "Product unit updated",
                                null,
                                updatedProductUnit,
                                StatusType.OK.name(),
                                HttpStatus.OK.value()),
                        "/api/admin/product/"+id+"/unit/"+idu),HttpStatus.OK);
    }
    @GetMapping("/product/{id}")
    public ResponseEntity<?> getProduct(@PathVariable("id") Long id) {
        if (!productService.existsProductById(id)) {
            throw new ResourceNotFoundException("product", "id", id.toString());
        }
        ProductDto product = productService.getProductById(id);
        return new ResponseEntity<>(
                new ApiResponse(
                        (new Date()).toString(),
                        new ApiResponseBody(
                                "Product",
                                null,
                                product,
                                StatusType.DATA_OK.name(),
                                HttpStatus.OK.value()),
                        "/api/admin/product/"+id),HttpStatus.OK);
    }

    @PutMapping("/product/{id}")
    public ResponseEntity<?> updateProduct(@PathVariable("id") Long id, @ModelAttribute @Valid ProductDto productDto) throws Exception {
        if (!productService.existsProductById(id)) {
            throw new ResourceNotFoundException("product", "id", id.toString());
        }
        ProductDto updatedProduct = productService.updateProduct(productDto,id);
        return new ResponseEntity<>(
                new ApiResponse(
                        (new Date()).toString(),
                        new ApiResponseBody(
                                "Product updated",
                                null,
                                updatedProduct,
                                StatusType.OK.name(),
                                HttpStatus.OK.value()),
                        "/api/admin/product/"+id),HttpStatus.OK);
    }
    @PutMapping("/product/{id}/status")
    public ResponseEntity<?> changeProductStatus(@PathVariable("id") Long id, @RequestParam("enabled") boolean enabled) {
        if (!productService.existsProductById(id)) {
            throw new ResourceNotFoundException("product", "id", id.toString());
        }
        productService.changeProductStatus(id,enabled);
        return new ResponseEntity<>(
                new ApiResponse(
                        (new Date()).toString(),
                        new ApiResponseBody(
                                "Product status changed",
                                null,
                                null,
                                StatusType.OK.name(),
                                HttpStatus.OK.value()),
                        "/api/admin/product/"+id+"/status"),HttpStatus.OK);
    }
    @PutMapping("/product/{id}/unit/{idu}/status")
    public ResponseEntity<?> changeProductUnitStatus(@PathVariable("id") Long id, @PathVariable("idu") Long idu, @RequestParam("enabled") boolean enabled) {
        if (!productService.existsProductById(id)) {
            throw new ResourceNotFoundException("product", "id", id.toString());
        }
        if(!productService.getProductById(id).isEnabled()){
            return new ResponseEntity<>(
                    new ApiResponse(
                            (new Date()).toString(),
                            new ApiResponseBody(
                                    "Product is not enabled",
                                    null,
                                    null,
                                    StatusType.ERROR.name(),
                                    HttpStatus.BAD_REQUEST.value()),
                            "/api/admin/product/"+id+"/unit/"+idu+"/status"),HttpStatus.BAD_REQUEST);
        }
        if(!productUnitService.existsProductUnit(idu)){
            throw new ResourceNotFoundException("product unit","id",idu.toString());
        }
        productUnitService.changeProductUnitStatus(idu,enabled);
        return new ResponseEntity<>(
                new ApiResponse(
                        (new Date()).toString(),
                        new ApiResponseBody(
                                "Product unit status changed",
                                null,
                                null,
                                StatusType.OK.name(),
                                HttpStatus.OK.value()),
                        "/api/admin/product/"+id+"/unit/"+idu+"/status"),HttpStatus.OK);
    }

}
