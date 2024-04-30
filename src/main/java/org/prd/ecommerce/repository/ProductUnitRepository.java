package org.prd.ecommerce.repository;

import org.prd.ecommerce.entities.dto.ProductUnitDto;
import org.prd.ecommerce.entities.entity.ProductUnit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductUnitRepository extends JpaRepository<ProductUnit, Long>{
    Long countByProductId(Long id);

    @Query("SELECT new org.prd.ecommerce.entities.dto.ProductUnitDto(p.id,p.enabled,p.color,p.other,p.stock,p.priceModifier) FROM ProductUnit p WHERE p.product.id = ?1")
    List<ProductUnitDto> findProductUnitByProductId(Long id);


    @Modifying
    @Query("UPDATE ProductUnit p SET p.enabled = :status WHERE p.product.id = :id")
    void changeEnabledStatusByProductId(@Param("id") Long id, @Param("status") boolean status);
    @Modifying
    @Query("UPDATE ProductUnit p SET p.enabled = :status WHERE p.id = :id")
    void changeEnabledStatusByProductUnitId(@Param("id") Long id,@Param("status") boolean status);
    @Modifying
    @Query("UPDATE ProductUnit p SET p.stock = p.stock - :quantity WHERE p.id = :id")
    void updateStockByProductUnitId(@Param("id") Long id,@Param("quantity") int quantity);

    @Modifying
    @Query("UPDATE ProductUnit p SET p.stock = p.stock + :quantity WHERE p.id = :id")
    void updateStockByProductUnitIdAdd(@Param("id") Long id,@Param("quantity") int quantity);


}
