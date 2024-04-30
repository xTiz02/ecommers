package org.prd.ecommerce.repository;

import org.prd.ecommerce.entities.entity.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductImageRepository extends JpaRepository<ProductImage, Long>{

    @Query("delete from ProductImage p where p.publicId in ?1")
    public void deleteByPublicIdIn(List<String> publicIds);
}
