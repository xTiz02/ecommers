package org.prd.ecommerce.entities.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "cart_item")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Double price;
    private Double discount;
    private Double discountPrice;
    private Integer quantity;
    private Double total;
    @ManyToOne(fetch = FetchType.LAZY, optional = false, cascade = CascadeType.MERGE)
    @JoinColumn(name = "product_unit_id", nullable = false)
    private ProductUnit productUnit;
//    @ManyToOne(fetch = FetchType.LAZY, optional = false, cascade = CascadeType.MERGE)
//    @JoinColumn(name = "user_id", nullable = false)
//    private UserEntity user;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;
}
