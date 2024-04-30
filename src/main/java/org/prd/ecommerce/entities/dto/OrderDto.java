package org.prd.ecommerce.entities.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.prd.ecommerce.config.util.enums.OrderStatus;

import java.util.UUID;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDto {
    private Long id;
    private UUID trackingNumber;
    private OrderStatus orderStatus;
    private Double totalAmount;
    private String address;
    private String user;
    private Integer totalItems;

}
