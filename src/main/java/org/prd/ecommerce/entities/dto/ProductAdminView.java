package org.prd.ecommerce.entities.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ProductAdminView {
    private Long id;
    private String name;
    private Double price;
    private String principalUrl;
    private Date createdAt;
    private boolean enabled;
    private String categoryName;
}
