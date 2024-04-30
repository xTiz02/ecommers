package org.prd.ecommerce.entities.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductUnitCustomerView {
    private Long id;
    private String color;
    private String other;
    private Integer stock;
    private Double priceModifier;
    private List<String> images;
}
