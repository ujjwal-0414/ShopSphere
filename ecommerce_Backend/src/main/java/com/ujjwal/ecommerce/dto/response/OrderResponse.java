package com.ujjwal.ecommerce.dto.response;

import com.ujjwal.ecommerce.entity.OrderItem;
import com.ujjwal.ecommerce.enums.OrderStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderResponse {

    private Long id;

    private OrderStatus status;

    private BigDecimal totalAmount;

    private String shippingFullName;

    private String shippingPhoneNumber;

    private String shippingAddressLine;

    private String shippingCity;

    private String shippingState;

    private String shippingCountry;

    private String shippingPostalCode;

    private List<OrderItemResponse> items;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}
