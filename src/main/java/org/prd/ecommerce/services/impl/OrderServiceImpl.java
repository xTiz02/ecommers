package org.prd.ecommerce.services.impl;

import org.prd.ecommerce.config.util.enums.OrderStatus;
import org.prd.ecommerce.config.util.mapper.EntitiesMapper;
import org.prd.ecommerce.config.util.mapper.EntitiesMapperImpl;
import org.prd.ecommerce.controller.exceptions.controll.BadRequestException;
import org.prd.ecommerce.entities.dto.CartItemDto;
import org.prd.ecommerce.entities.dto.OrderDto;
import org.prd.ecommerce.entities.entity.*;
import org.prd.ecommerce.repository.CartRepository;
import org.prd.ecommerce.repository.OrderRepository;
import org.prd.ecommerce.repository.ProductUnitRepository;
import org.prd.ecommerce.repository.UserEntityRepository;
import org.prd.ecommerce.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private EntitiesMapper entitiesMapper;
    @Autowired
    private UserEntityRepository userEntityRepository;

    @Autowired
    private ProductUnitRepository productUnitRepository;
    @Override
    @Transactional
    public OrderDto addCartItem(List<CartItemDto> cartItemDtoList) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity user = userEntityRepository.findByEmail(username).orElse(null);
        Order principalOrder = new Order();
        principalOrder.setUser(user);
        principalOrder.setOrderStatus(OrderStatus.PENDING);
        principalOrder.setTrackingId(UUID.randomUUID());
        principalOrder.setOrderDescription("Order has been created");
        principalOrder.setAddress("Address");
        principalOrder.setDate(new Date());
        principalOrder.setTotalAmount(0.0);
        principalOrder.setTotalItems(0);
        Order createdOrder = orderRepository.save(principalOrder);
        List<CartItem> cartItemList = new ArrayList<>();
        Double[] totalAmount = {0.0};
        cartItemDtoList.stream().forEach(cartItemDto -> {
            ProductUnit productUnit = productUnitRepository.findById(cartItemDto.getProductUnitId()).orElse(null);
            Product product = productUnit.getProduct();
            if(productUnit.getStock() < cartItemDto.getQuantity()){
                if(productUnit.getColor()!=null){
                    throw new BadRequestException(product.getName()+" with color "+productUnit.getColor()+ " has only "+productUnit.getStock()+" items in stock");
                }else{
                    throw new BadRequestException(product.getName()+" has only "+productUnit.getStock()+" items in stock");
                }
            }

            Promotion promotion = null;
            Double discountPrice = null;
            Double total = null;
            if(product.getPromotions().stream().anyMatch(pro -> pro.isEnabled())){
                promotion = product.getPromotions().stream().filter(pro -> pro.isEnabled()).findFirst().orElse(null);
                discountPrice = product.getPrice() - (product.getPrice() * promotion.getDiscount()/100);
                total = cartItemDto.getQuantity()*(discountPrice);
                totalAmount[0] += total;

                System.out.println(total);
            }else{
                total = product.getPrice() * cartItemDto.getQuantity();
                totalAmount[0] += total;
                System.out.println(total);
            }
            CartItemDto cartDto = new CartItemDto();
            if(promotion != null){
                cartDto = new CartItemDto(null,productUnit.getId(),product.getName(),product.getPrice(),cartItemDto.getQuantity(),promotion.getDiscount(),discountPrice,total);
            }else{
                cartDto = new CartItemDto(null,productUnit.getId(),product.getName(),product.getPrice(),cartItemDto.getQuantity(),null,null,total);
            }
            CartItem cartItem = entitiesMapper.cartItemDtoToCartItem(cartDto);
            cartItem.setProductUnit(productUnit);
            cartItem.setOrder(createdOrder);
            cartItemList.add(cartItem);
            productUnitRepository.updateStockByProductUnitId(cartItemDto.getProductUnitId(),cartItemDto.getQuantity());
        });
        System.out.println(totalAmount[0]);
        createdOrder.setTotalAmount(totalAmount[0]);
        createdOrder.setTotalItems(cartItemList.size());
        createdOrder.setCartItems(cartItemList);
        Order updateOrder = orderRepository.save(createdOrder);
        return entitiesMapper.orderToOrderDto(updateOrder);
    }
}
