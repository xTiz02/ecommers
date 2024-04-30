package org.prd.ecommerce.entities.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class CategoryCustomerView {
    private Long id;
    private String name;
    private String imgUrl;
    private Integer countProducts;

    public CategoryCustomerView(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public CategoryCustomerView(Long id, String name, String imgUrl) {
        this.id = id;
        this.name = name;
        this.imgUrl = imgUrl;
    }
}
