package org.prd.ecommerce.config.util.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.prd.ecommerce.entities.dto.*;
import org.prd.ecommerce.entities.entity.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface EntitiesMapper {

    @Mappings({
            @Mapping(target="userRole",constant = "CUSTOMER"),
            @Mapping(target="enabled",constant = "true"),
            @Mapping(target="accountNonExpired",constant = "true"),
            @Mapping(target="accountNonLocked",constant = "true"),
            @Mapping(target="credentialsNonExpired",constant = "true")
    })
    UserEntityDto singupRequestToCustomerUserEntityDto(SignupRequest signupRequest);

    @Mappings({
            @Mapping(target="password",ignore = true)
    })
    UserEntity userEntityDtoToUserEntity(UserEntityDto userEntityDto);

    @Mappings({
            @Mapping(target="password",ignore = true)
    })
    UserEntityDto userEntityToUserEntityDto(UserEntity userEntity);



    //category
    @Mappings({
            @Mapping(target="img",ignore = true)
    })
    CategoryDto categoryToCategoryDto(Category Category);
    //change Multipart To byte[]
    Category categoryDtoToCategory(CategoryDto categoryDto);

    List<CategoryDto> categoryListToCategoryDtoList(List<Category> categories);


    //product
    @Mappings({
            @Mapping(target="enabled",constant = "false"),
    })
    Product productDtoToProduct(ProductDto productDto);

    @Mappings({
            @Mapping(target="enabled",source = "product.enabled"),
            @Mapping(target="createdAt", source = "product.createdAt"),
            @Mapping(target="id", source = "product.id"),
            @Mapping(target="name", source = "productDto.name"),
            @Mapping(target="description", source = "productDto.description"),
            @Mapping(target="characteristics", source = "productDto.characteristics"),
            @Mapping(target="specifications", source = "productDto.specifications"),
            @Mapping(target="price", source = "productDto.price"),
            @Mapping(target="principalUrl", source = "productDto.principalUrl"),
            @Mapping(target="lastModifiedAt", source = "product.lastModifiedAt"),
            @Mapping(target="category", source = "product.category"),
    })
    Product productDtoToProductToUpdate(ProductDto productDto,Product product);

    @Mappings({
            @Mapping(target="images",ignore = true),
            @Mapping(target="productId",source = "productUnit.product.id")
    })
    ProductUnitDto productUnitToProductUnitDto(ProductUnit productUnit);
    @Mappings({
            @Mapping(target="images",ignore = true),
            @Mapping(target="categoryId",source = "category.id"),
            @Mapping(target="categoryName",source = "category.name")
    })
    ProductDto productToProductDto(Product product);

    List<ProductDto> productListToProductDtoList(List<Product> products);

    @Mappings({
            @Mapping(target="id",source = "product.id"),
            @Mapping(target="name",source = "product.name"),
            @Mapping(target="price",source = "product.price"),
            @Mapping(target="categoryName",source = "product.category.name"),
            @Mapping(target="imgUrl",source = "product.principalUrl"),
            @Mapping(target="units",source = "productUnitCustomerViewList"),
            @Mapping(target="description",source = "product.description"),
            @Mapping(target="characteristics",source = "product.characteristics"),
            @Mapping(target="specifications",source = "product.specifications"),
            @Mapping(target="promotion",source = "promotionDto")
    })
    ProductCustomerView productToproductCustomerView(Product product,List<ProductUnitCustomerView> productUnitCustomerViewList,PromotionDto promotionDto);

    @Mappings({
            @Mapping(target="id",source = "productUnit.id"),
            @Mapping(target="color",source = "productUnit.color"),
            @Mapping(target="other",source = "productUnit.other"),
            @Mapping(target="priceModifier",source = "productUnit.priceModifier"),
            @Mapping(target="stock",source = "productUnit.stock"),
            @Mapping(target="images",ignore = true)
    })
    ProductUnitCustomerView productUnitToProductUnitView(ProductUnit productUnit);

    @Mappings({
            @Mapping(target="id",source = "product.id"),
            @Mapping(target="name",source = "product.name"),
            @Mapping(target="price",source = "product.price"),
            @Mapping(target="categoryName",source = "product.category.name"),
            @Mapping(target="imgUrl",source = "product.principalUrl"),
            @Mapping(target="promotion",source = "promotionDto")
    })
    ProductCustomerView productToProductCustomerView(Product product,PromotionDto promotionDto);




    //productUnit
    @Mappings({
            @Mapping(target="enabled",constant = "false")
    })
    ProductUnit productUnitDtoToProductUnit(ProductUnitDto productUnitDto);

    @Mappings({
            @Mapping(target="id",source = "productUnit.id"),
            @Mapping(target="enabled",source = "productUnit.enabled"),
            @Mapping(target="color",source = "productUnitDto.color"),
            @Mapping(target="other",source = "productUnitDto.other"),
            @Mapping(target="stock",source = "productUnitDto.stock"),
            @Mapping(target="priceModifier",source = "productUnitDto.priceModifier"),
            @Mapping(target="product",source = "productUnit.product")
    })
    ProductUnit productUnitDtoToProductUnitToUpdate(ProductUnitDto productUnitDto,ProductUnit productUnit);


    //promotion
    @Mappings({
            @Mapping(target="productId",source = "product.id")
    })
    PromotionDto promotionToPromotionDto(Promotion promotion);


    Promotion promotionDtoToPromotion(PromotionDto promotionDto);

    List<PromotionDto> promotionListToPromotionDtoList(List<Promotion> promotions);

    //cart
    CartItem cartItemDtoToCartItem(CartItemDto cartItemDto);

    //order
    @Mappings({
            @Mapping(target="trackingNumber",source = "order.trackingId"),
            @Mapping(target="orderStatus",source = "order.orderStatus"),
            @Mapping(target="totalAmount",source = "order.totalAmount"),
            @Mapping(target="address",source = "order.address"),
            @Mapping(target="user",source = "order.user.email"),
            @Mapping(target="totalItems",source = "order.totalItems")
    })
    OrderDto orderToOrderDto(Order order);
}
