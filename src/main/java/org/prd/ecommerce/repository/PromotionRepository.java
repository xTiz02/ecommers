package org.prd.ecommerce.repository;

import org.prd.ecommerce.entities.dto.PromotionDto;
import org.prd.ecommerce.entities.entity.Promotion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface PromotionRepository extends JpaRepository<Promotion, Long> {

    Long countPromotionByProductId(Long id);
    @Query("SELECT new org.prd.ecommerce.entities.dto.PromotionDto(p.id,p.discount,p.startDate,p.endDate,p.product.id,p.enabled) FROM Promotion p WHERE p.product.id = ?1")
    List<PromotionDto> getAllPromotionsByProductId(Long id);

    @Query("SELECT CASE WHEN COUNT(p) > 0 THEN true ELSE false END FROM Promotion p WHERE p.product.id = ?1 AND ((p.startDate <= ?2 AND p.endDate >= ?2) OR (p.startDate <= ?3 AND p.endDate >= ?3))")
    boolean existConflictForDate(Long productId, Date startDate, Date endDate);
}
