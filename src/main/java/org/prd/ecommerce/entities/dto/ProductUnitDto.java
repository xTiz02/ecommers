package org.prd.ecommerce.entities.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.prd.ecommerce.config.util.validation.ValidateMultipartFiles;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductUnitDto {
    private Long id;
    @ValidateMultipartFiles
    private MultipartFile[] images;
    List<String> imagesReturn;
    private boolean enabled;
    private Long productId;
    private String color;
    private String other;
    private Integer stock;
    private Double priceModifier;

    public ProductUnitDto(Long id,boolean enabled, String color, String other, Integer stock, Double priceModifier) {
        this.id = id;
        this.enabled = enabled;
        this.color = color;
        this.other = other;
        this.stock = stock;
        this.priceModifier = priceModifier;
    }
}
