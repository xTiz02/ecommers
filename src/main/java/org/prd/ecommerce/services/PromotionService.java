package org.prd.ecommerce.services;

import org.prd.ecommerce.entities.dto.PromotionDto;

import java.util.Date;
import java.util.List;

public interface PromotionService {

    PromotionDto createPromotion(PromotionDto promotionDto);

    Long countPromotionByProductId(Long id);

    List<PromotionDto> getAllPromotionsByProductId(Long id);

    void deletePromotion(Long id);

    boolean existsPromotionById(Long id);

    PromotionDto getPromotionById(Long id);

    PromotionDto updatePromotion(PromotionDto promotionDto);

    boolean existConflictForDate(Long productId, Date startDate, Date endDate);
}
