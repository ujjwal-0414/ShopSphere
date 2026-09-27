package com.ujjwal.ecommerce.service.impl;

import com.ujjwal.ecommerce.dto.request.UpdateOrderStatusRequest;
import com.ujjwal.ecommerce.dto.response.OrderResponse;
import com.ujjwal.ecommerce.exception.BadRequestException;
import com.ujjwal.ecommerce.exception.ResourceNotFoundException;
import com.ujjwal.ecommerce.entity.Order;
import com.ujjwal.ecommerce.entity.OrderItem;
import com.ujjwal.ecommerce.entity.Product;
import com.ujjwal.ecommerce.enums.OrderStatus;
import com.ujjwal.ecommerce.mapper.OrderMapper;
import com.ujjwal.ecommerce.repository.OrderRepository;
import com.ujjwal.ecommerce.repository.ProductRepository;
import com.ujjwal.ecommerce.service.AdminOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminOrderServiceImpl implements AdminOrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final OrderMapper orderMapper;

    @Override
    @Transactional(readOnly = true)
    public List<OrderResponse> getAllOrders() {

        return orderRepository.findAllWithItemsAndProducts()
                .stream()
                .map(orderMapper::toOrderResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public OrderResponse getOrderById(Long orderId) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        return orderMapper.toOrderResponse(order);
    }

    @Override
    @Transactional
    public OrderResponse updateOrderStatus(
            Long orderId,
            UpdateOrderStatusRequest request
    ) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        OrderStatus currentStatus = order.getStatus();
        OrderStatus requestedStatus = request.getStatus();

        if (!isValidTransition(
                currentStatus,
                requestedStatus
        )) {
            throw new BadRequestException(
                    "Invalid order status transition: "
                            + currentStatus
                            + " -> "
                            + requestedStatus
            );
        }

        order.setStatus(requestedStatus);

        Order savedOrder = orderRepository.save(order);

        return orderMapper.toOrderResponse(savedOrder);
    }

    @Override
    @Transactional
    public OrderResponse cancelOrder(Long orderId) {

        // Admin can cancel any customer's order,so we do NOT perform a user ownership check here.
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        // Only PENDING and CONFIRMED orders can be cancelled.
        if (order.getStatus() != OrderStatus.PENDING &&
                order.getStatus() != OrderStatus.CONFIRMED) {

            throw new BadRequestException(
                    "Order cannot be cancelled in status: "
                            + order.getStatus());
        }

        //Restore the stock for every product in the order.
        for (OrderItem orderItem : order.getItems()) {

            // Lock the product row before changing stock
            Product product = productRepository
                    .findByIdWithLock(
                            orderItem.getProduct().getId()
                    )
                    .orElseThrow(() ->
                            new ResourceNotFoundException(
                                    "Product not found"));
            // Restore ordered quantity
            product.setStock(
                    product.getStock()
                            + orderItem.getQuantity()
            );
        }

        // Change the order status to CANCELLED
        order.setStatus(OrderStatus.CANCELLED);

        // Product stock changes are also persisted because the Product entities are managed by the current transaction.
        Order savedOrder = orderRepository.save(order);

        // Convert entity into response DTO
        return orderMapper.toOrderResponse(savedOrder);
    }

    private boolean isValidTransition(
            OrderStatus currentStatus,
            OrderStatus requestedStatus
    ) {

        if (currentStatus == requestedStatus) {
            return false;
        }

        return switch (currentStatus) {

            case PENDING ->
                    requestedStatus == OrderStatus.CONFIRMED
                            || requestedStatus == OrderStatus.CANCELLED;

            case CONFIRMED ->
                    requestedStatus == OrderStatus.SHIPPED
                            || requestedStatus == OrderStatus.CANCELLED;

            case SHIPPED ->
                    requestedStatus == OrderStatus.DELIVERED;

            case DELIVERED,
                 CANCELLED ->
                    false;
        };
    }
}