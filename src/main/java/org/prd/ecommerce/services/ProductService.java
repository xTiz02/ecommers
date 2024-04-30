package org.prd.ecommerce.services;

import org.prd.ecommerce.entities.dto.*;
import org.prd.ecommerce.entities.entity.Product;
import org.prd.ecommerce.entities.entity.ProductUnit;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface ProductService {
    ProductUnitDto createProduct(ProductDto productDto) throws Exception;
    List<ProductDto> getAllProducts();
    boolean existsProductById(Long id);
    ProductDto updateProduct(ProductDto productDto, Long id) throws Exception;
    ProductDto getProductById(Long id);

    List<ProductCustomerView> getFirstNumProductsOrderByCreatedAtDesc(Integer num);
    List<ProductCustomerView> getFirstNumProductsOrderByPromotionsDiscountDesc(Integer num);

    void changeProductStatus(Long id, boolean status);
    PageResponse getPageWithSimpleProductsInformation(int page, int size);
    PageResponse getPageWithSimpleProductsInformationByNameContaining(String name, int page, int size);
    void deleteProduct(Long id) throws Exception;
//    PageResponse getAllProductsPageByName(String name,int page, int size);
//
//    PageResponse getAllProductsWithPagination(int page, int size);
//
//    PageResponse getAllProductsWithPaginationAndSorting(int page, int size, String sort, String field);
    PageResponse getSimpleProductsViewPage (String title,int page, int size);
    ProductCustomerView getProductCustomerView(Long id);
    PageResponse getProductsByFilter(FilterData filterData);
}
