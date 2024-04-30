package org.prd.ecommerce.entities.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "product")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 40)
    private String name;
    @Column(columnDefinition = "TEXT")
    private String characteristics;
    @Column(columnDefinition = "TEXT")
    private String specifications;
    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private Double price;

    private String principalUrl;

    private boolean enabled;

    @Temporal(TemporalType.DATE)
    private Date createdAt;

    @Temporal(TemporalType.TIMESTAMP)
    private Date lastModifiedAt;

    @ManyToOne(fetch = FetchType.LAZY, optional = false,cascade = {CascadeType.MERGE,CascadeType.PERSIST,CascadeType.REFRESH})//optional = false, no se puede crear un producto sin categoria
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY, cascade = CascadeType.ALL) //, si se elimina un producto, se eliminan todas sus unidades
    private List<ProductUnit> productUnits;

    @OneToMany(mappedBy = "product",fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Promotion> promotions;

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", characteristics='" + characteristics + '\'' +
                ", specifications='" + specifications + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", principalUrl='" + principalUrl + '\'' +
                ", enabled=" + enabled +
                ", createdAt=" + createdAt +
                ", lastModifiedAt=" + lastModifiedAt +
                ", category=" + category +
                ", productUnits=" + productUnits +
                ", promotions=" + promotions +
                '}';
    }
}
