package com.ujjwal.ecommerce.service;

import com.ujjwal.ecommerce.dto.request.UpdateOrderStatusRequest;
import com.ujjwal.ecommerce.dto.response.OrderResponse;

import java.util.List;

public interface AdminOrderService {

    List<OrderResponse> getAllOrders();

    OrderResponse getOrderById(Long orderId);

    OrderResponse updateOrderStatus(
            Long orderId,
            UpdateOrderStatusRequest request
    );

    OrderResponse cancelOrder(Long orderId);

}