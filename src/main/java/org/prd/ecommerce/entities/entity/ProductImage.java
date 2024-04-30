package org.prd.ecommerce.entities.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "product_image")
public class ProductImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String publicId;
    private String name;
    private String url;
    private String tag;
    private Long size;
    private String format;
    @ManyToOne(fetch = FetchType.LAZY, optional = false,cascade = CascadeType.REFRESH)
    @JoinColumn(name = "product_unit_id", nullable = false)
    //@OnDelete(action = OnDeleteAction.CASCADE)si se elimina una unidad, se eliminan todas sus imagenes
    private ProductUnit productUnit;

    @Override
    public String toString() {
        return "ProductImage{" +
                "id=" + id +
                ", publicId='" + publicId + '\'' +
                ", name='" + name + '\'' +
                ", url='" + url + '\'' +
                ", tag='" + tag + '\'' +
                ", size=" + size +
                ", format='" + format + '\'' +
                '}';
    }
}
