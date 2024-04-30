package org.prd.ecommerce.entities.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "product_unit")
public class ProductUnit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String color;
    private String other;
    private Integer stock;
    private Double priceModifier;
    private boolean enabled;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)//optional = false, no se puede crear un producto sin categoria
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @OneToMany(mappedBy = "productUnit", fetch = FetchType.LAZY, cascade = CascadeType.ALL,orphanRemoval = true)
    private List<ProductImage> imagesProducts;

    public ProductUnit(String color, String other, Integer stock, Double priceModifier, boolean enabled, Product product) {
        this.color = color;
        this.other = other;
        this.stock = stock;
        this.priceModifier = priceModifier;
        this.enabled = enabled;
        this.product = product;
    }

    @Override
    public String toString() {
        return "ProductUnit{" +
                "id=" + id +
                ", color='" + color + '\'' +
                ", other='" + other + '\'' +
                ", stock=" + stock +
                ", priceModifier=" + priceModifier +
                ", enabled=" + enabled +
                ", imagesProducts=" + imagesProducts +
                '}';
    }
}
