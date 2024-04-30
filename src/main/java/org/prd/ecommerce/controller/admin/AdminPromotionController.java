package org.prd.ecommerce.controller.admin;

import org.prd.ecommerce.config.util.enums.StatusType;
import org.prd.ecommerce.controller.exceptions.controll.BadRequestException;
import org.prd.ecommerce.controller.exceptions.controll.ResourceNotFoundException;
import org.prd.ecommerce.entities.dto.ApiResponse;
import org.prd.ecommerce.entities.dto.ApiResponseBody;
import org.prd.ecommerce.entities.dto.ProductUnitDto;
import org.prd.ecommerce.entities.dto.PromotionDto;
import org.prd.ecommerce.services.ProductService;
import org.prd.ecommerce.services.PromotionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminPromotionController {

    private final PromotionService promotionService;
    private final ProductService productService;

    public AdminPromotionController(PromotionService promotionService, ProductService productService) {
        this.promotionService = promotionService;
        this.productService = productService;
    }

    @PostMapping("/promotion")
    public ResponseEntity<?> createPromotion(@RequestBody PromotionDto promotionDto) {
        if(!productService.existsProductById(promotionDto.getProductId())){
            throw new ResourceNotFoundException("Product", "id", promotionDto.getProductId());
        }
        if(promotionService.existConflictForDate(promotionDto.getProductId(), promotionDto.getStartDate(), promotionDto.getEndDate())) {
            throw new BadRequestException("Promotion conflict for date");
        }
        PromotionDto createdPromotion = promotionService.createPromotion(promotionDto);
        return new ResponseEntity<>(
                new ApiResponse(
                        (new Date()).toString(),
                        new ApiResponseBody(
                                "Promotion Created",
                                null,
                                createdPromotion,
                                StatusType.OK.name(),
                                HttpStatus.CREATED.value()),
                        "/api/admin/promotion"),HttpStatus.CREATED);

    }
    @GetMapping("/promotions/product/{id}/count")
    public ResponseEntity<?> countPromotionByProductId(@PathVariable Long id) {
        if(!productService.existsProductById(id)){
            throw new ResourceNotFoundException("Product", "id", id);
        }
        Long count = promotionService.countPromotionByProductId(id);
        return new ResponseEntity<>(
                new ApiResponse(
                        (new Date()).toString(),
                        new ApiResponseBody(
                                "Promotion count",
                                null,
                                count,
                                StatusType.DATA_OK.name(),
                                HttpStatus.OK.value()),
                        "/api/admin/promotion/product/"+id+"/count"),HttpStatus.OK);
    }
    @GetMapping("/promotions/product/{id}")
    public ResponseEntity<?> getPromotionsByProductId(@PathVariable("id") Long id) {
        if (!productService.existsProductById(id)) {
            throw new ResourceNotFoundException("product", "id", id.toString());
        }
        List<PromotionDto> promotionDtos = promotionService.getAllPromotionsByProductId(id);
        if (promotionDtos == null || promotionDtos.isEmpty()) {
            throw new ResourceNotFoundException("promotions");
        }
        return new ResponseEntity<>(
                new ApiResponse(
                        (new Date()).toString(),
                        new ApiResponseBody(
                                "Promotions for product",
                                null,
                                promotionDtos,
                                StatusType.DATA_OK.name(),
                                HttpStatus.OK.value()),
                        "/api/admin/promotions/product/"+id),HttpStatus.OK);
    }
    @DeleteMapping("/promotion/{id}")
    public ResponseEntity<?> deletePromotion(@PathVariable Long id) {
        if(!promotionService.existsPromotionById(id)){
            throw new ResourceNotFoundException("Promotion", "id", id);
        }
        promotionService.deletePromotion(id);
        return new ResponseEntity<>(
                new ApiResponse(
                        (new Date()).toString(),
                        new ApiResponseBody(
                                "Promotion Deleted",
                                null,
                                null,
                                StatusType.OK.name(),
                                HttpStatus.OK.value()),
                        "/api/admin/promotion/"+id),HttpStatus.OK);
    }

    @GetMapping("/promotion/{id}")
    public ResponseEntity<?> getPromotionById(@PathVariable("id") Long id) {
        if(!promotionService.existsPromotionById(id)){
            throw new ResourceNotFoundException("Promotion", "id", id);
        }
        PromotionDto promotionDto = promotionService.getPromotionById(id);
        return new ResponseEntity<>(
                new ApiResponse(
                        (new Date()).toString(),
                        new ApiResponseBody(
                                "Promotion",
                                null,
                                promotionDto,
                                StatusType.DATA_OK.name(),
                                HttpStatus.OK.value()),
                        "/api/admin/promotion/"+id),HttpStatus.OK);
    }

    @PutMapping("/promotion/{id}")
    public ResponseEntity<?> updatePromotion(@PathVariable("id") Long id, @RequestBody PromotionDto promotionDto) {
        if(!promotionService.existsPromotionById(id)){
            throw new ResourceNotFoundException("Promotion", "id", id);
        }
        if(!productService.existsProductById(promotionDto.getProductId())) {
            throw new ResourceNotFoundException("Product", "id", promotionDto.getProductId());
        }
        PromotionDto updatedPromotion = promotionService.updatePromotion(promotionDto);
        return new ResponseEntity<>(
                new ApiResponse(
                        (new Date()).toString(),
                        new ApiResponseBody(
                                "Promotion Updated",
                                null,
                                updatedPromotion,
                                StatusType.OK.name(),
                                HttpStatus.OK.value()),
                        "/api/admin/promotion/"+id),HttpStatus.OK);
    }
}
