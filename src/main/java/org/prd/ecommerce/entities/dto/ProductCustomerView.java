package org.prd.ecommerce.entities.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductCustomerView {
    private Long id;
    private String name;
    private Double price;
    private String categoryName;

    public ProductCustomerView(Long id, String name, Double price, String categoryName, String imgUrl, PromotionDto promotion) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.categoryName = categoryName;
        this.imgUrl = imgUrl;
        this.promotion = promotion;
    }

    private String imgUrl;
    private PromotionDto promotion;
    private List<ProductUnitCustomerView> units;
    private String description;
    private String characteristics;
    private String specifications;


    public ProductCustomerView(Long id, String name, Double price, String categoryName, String imgUrl) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.categoryName = categoryName;
        this.imgUrl = imgUrl;
    }
}
