package com.ujjwal.ecommerce.service;

import com.ujjwal.ecommerce.dto.request.PlaceOrderRequest;
import com.ujjwal.ecommerce.dto.response.OrderResponse;

import java.util.List;

public interface OrderService {

    OrderResponse placeOrder(PlaceOrderRequest request);

    List<OrderResponse> getMyOrders();

    OrderResponse getOrderById(Long orderId);

    OrderResponse cancelOrder(Long orderId);

}
