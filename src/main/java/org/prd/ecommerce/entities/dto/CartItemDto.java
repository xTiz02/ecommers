package org.prd.ecommerce.entities.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartItemDto {
    private Long id;
    private Long productUnitId;
    private String name;
    private Double price;
    private Integer quantity;
    private Double discount;
    private Double discountPrice;
    private Double total;

    @Override
    public String toString() {
        return "CartItemDto{" +
                "id=" + id +
                ", productUnitId=" + productUnitId +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", quantity=" + quantity +
                ", discount=" + discount +
                ", discountPrice=" + discountPrice +
                ", total=" + total +
                '}';
    }
}
