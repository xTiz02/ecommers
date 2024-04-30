package org.prd.ecommerce.controller.customer;

import org.prd.ecommerce.config.util.enums.StatusType;
import org.prd.ecommerce.controller.exceptions.controll.BadRequestException;
import org.prd.ecommerce.entities.dto.ApiResponse;
import org.prd.ecommerce.entities.dto.ApiResponseBody;
import org.prd.ecommerce.entities.dto.CartItemDto;
import org.prd.ecommerce.entities.dto.OrderDto;
import org.prd.ecommerce.repository.ProductUnitRepository;
import org.prd.ecommerce.services.OrderService;
import org.prd.ecommerce.services.ProductUnitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/customer/logged")
public class CustomerOrderController {
    @Autowired
    private OrderService orderService;
    @Autowired
    private ProductUnitService productUnitService;
    @PostMapping("/order")
    public ResponseEntity<?> createOrder(@RequestBody List<CartItemDto> cartItemDtoList) {
        cartItemDtoList.forEach(cartItemDto -> {
            if(cartItemDto.getQuantity() <= 0){
                throw new BadRequestException("Quantity must be greater than 0");
            }
            if(!productUnitService.existsProductUnit(cartItemDto.getProductUnitId())){
                throw new BadRequestException("Product unit with id " + cartItemDto.getProductUnitId() + " does not exist");
            }
        });
        OrderDto orderDto = orderService.addCartItem(cartItemDtoList);
        return new ResponseEntity<>(
                new ApiResponse(
                        (new Date()).toString(),
                        new ApiResponseBody(
                                "Order created",
                                null,
                                orderDto,
                                StatusType.DATA_OK.name(),
                                HttpStatus.OK.value()),
                        "/api/customer/order"),
                HttpStatus.OK);
    }
}
