package com.ujjwal.ecommerce.dto.response;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderItemResponse {

    private Long id;

    private Long productId;

    private String productName;

    private Integer quantity;

    private BigDecimal price; // bcz using double/float can introduce precision problem

    private BigDecimal subtotal;

}
