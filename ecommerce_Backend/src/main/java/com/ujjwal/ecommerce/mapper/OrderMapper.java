package com.ujjwal.ecommerce.mapper;

import com.ujjwal.ecommerce.dto.response.OrderItemResponse;
import com.ujjwal.ecommerce.dto.response.OrderResponse;
import com.ujjwal.ecommerce.entity.Order;
import com.ujjwal.ecommerce.entity.OrderItem;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OrderMapper {

    public OrderItemResponse toOrderItemResponse(OrderItem orderItem) {
        OrderItemResponse response = new OrderItemResponse();
        response.setId(orderItem.getId());
        response.setProductId(orderItem.getProduct().getId());
        response.setProductName(orderItem.getProduct().getName());
        response.setPrice(orderItem.getPrice()); // it is bcz current product price can be different from the price of the day customer bought it
        response.setQuantity(orderItem.getQuantity());
        response.setSubtotal(orderItem.getSubtotal());

        return response;
    }

    public OrderResponse toOrderResponse(Order order) {
        OrderResponse response = new OrderResponse();
        response.setId(order.getId());
        response.setStatus(order.getStatus());
        response.setTotalAmount(order.getTotalAmount());

        response.setShippingFullName(
                order.getShippingFullName()
        );

        response.setShippingPhoneNumber(
                order.getShippingPhoneNumber()
        );

        response.setShippingAddressLine(
                order.getShippingAddressLine()
        );

        response.setShippingCity(
                order.getShippingCity()
        );

        response.setShippingState(
                order.getShippingState()
        );

        response.setShippingCountry(
                order.getShippingCountry()
        );

        response.setShippingPostalCode(
                order.getShippingPostalCode()
        );

        List<OrderItemResponse> items =
                order.getItems()
                        .stream()
                        .map(this::toOrderItemResponse)
                        .toList();

        response.setItems(items);

        response.setCreatedAt(order.getCreatedAt());
        response.setUpdatedAt(order.getUpdatedAt());

        return response;
    }

}
