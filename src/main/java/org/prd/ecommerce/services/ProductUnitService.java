package org.prd.ecommerce.services;

import org.prd.ecommerce.entities.dto.ProductUnitDto;

import java.util.List;

public interface ProductUnitService {

    ProductUnitDto createProductUnit(ProductUnitDto productUnitDto,Long id) throws Exception;
    void changeProductUnitStatus(Long idUnit, boolean status);
    Long countProductUnitByProductId(Long id);
    ProductUnitDto getProductUnitById(Long productId,Long productUnitId);
    ProductUnitDto updateProductUnit(ProductUnitDto productUnitDto,Long id) throws Exception;

    List<ProductUnitDto> getProductUnitByProductId(Long id);

    boolean existsProductUnit(Long id);
}
