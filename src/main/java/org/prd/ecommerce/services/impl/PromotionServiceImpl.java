package org.prd.ecommerce.services.impl;

import org.prd.ecommerce.config.util.mapper.EntitiesMapper;
import org.prd.ecommerce.entities.dto.PromotionDto;
import org.prd.ecommerce.entities.entity.Product;
import org.prd.ecommerce.entities.entity.Promotion;
import org.prd.ecommerce.repository.ProductRepository;
import org.prd.ecommerce.repository.PromotionRepository;
import org.prd.ecommerce.services.PromotionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class PromotionServiceImpl implements PromotionService {
    @Autowired
    private PromotionRepository promotionRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private EntitiesMapper entitiesMapper;
    @Override
    @Transactional
    public PromotionDto createPromotion(PromotionDto promotionDto) {
        Product product = productRepository.findById(promotionDto.getProductId()).orElse(null);
        Promotion promotion1 = entitiesMapper.promotionDtoToPromotion(promotionDto);
        if(promotionDto.getStartDate().before(new Date()) || promotionDto.getStartDate().equals(new Date())){
            promotion1.setEnabled(true);
        }else {
            promotion1.setEnabled(false);
        }
        promotion1.setProduct(product);
        return entitiesMapper.promotionToPromotionDto(promotionRepository.save(promotion1));
    }

    @Override
    @Transactional(readOnly = true)
    public Long countPromotionByProductId(Long id) {
        return promotionRepository.countPromotionByProductId(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PromotionDto> getAllPromotionsByProductId(Long id) {
        return promotionRepository.getAllPromotionsByProductId(id);
    }

    @Override
    @Transactional
    public void deletePromotion(Long id) {
        promotionRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsPromotionById(Long id) {
        return promotionRepository.existsById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public PromotionDto getPromotionById(Long id) {

        return entitiesMapper.promotionToPromotionDto(promotionRepository.findById(id).orElse(null));
    }

    @Override
    @Transactional
    public PromotionDto updatePromotion(PromotionDto promotionDto) {
        Promotion promotion = entitiesMapper.promotionDtoToPromotion(promotionDto);
        promotion.setProduct(productRepository.findById(promotionDto.getProductId()).orElse(null));
        if(promotionDto.getStartDate().before(new Date()) || promotionDto.getStartDate().equals(new Date())){
            promotion.setEnabled(true);
        }else {
            promotion.setEnabled(false);
        }
        return entitiesMapper.promotionToPromotionDto(promotionRepository.save(promotion));
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existConflictForDate(Long productId, Date startDate, Date endDate) {
        return promotionRepository.existConflictForDate(productId, startDate, endDate);
    }
}
