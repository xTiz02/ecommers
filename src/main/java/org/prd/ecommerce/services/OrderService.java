package org.prd.ecommerce.services;

import org.prd.ecommerce.entities.dto.CartItemDto;
import org.prd.ecommerce.entities.dto.OrderDto;

import java.util.List;

public interface OrderService {
    OrderDto addCartItem(List<CartItemDto> cartItemDtoList);
}
