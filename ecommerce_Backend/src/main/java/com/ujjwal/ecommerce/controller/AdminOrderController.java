package com.ujjwal.ecommerce.controller;

import com.ujjwal.ecommerce.dto.request.UpdateOrderStatusRequest;
import com.ujjwal.ecommerce.dto.response.OrderResponse;
import com.ujjwal.ecommerce.service.AdminOrderService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/orders")
@RequiredArgsConstructor
@Validated
public class AdminOrderController {

    private final AdminOrderService adminOrderService;

    @GetMapping
    public ResponseEntity<List<OrderResponse>> getAllOrders() {

        List<OrderResponse> orders =
                adminOrderService.getAllOrders();

        return ResponseEntity.ok(orders);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponse> getOrderById(
            @PathVariable @Positive(message = "Order ID must be positive") Long orderId
    ) {

        OrderResponse response =
                adminOrderService.getOrderById(orderId);

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{orderId}/status")
    public ResponseEntity<OrderResponse> updateOrderStatus(
            @PathVariable @Positive(message = "Order ID must be positive") Long orderId,
            @Valid @RequestBody UpdateOrderStatusRequest request
    ) {

        OrderResponse response =
                adminOrderService.updateOrderStatus(
                        orderId,
                        request
                );

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{orderId}/cancel")
    public ResponseEntity<OrderResponse> cancelOrder(
            @PathVariable @Positive(message = "Order ID must be positive") Long orderId
    ) {

        OrderResponse response =
                adminOrderService.cancelOrder(orderId);

        return ResponseEntity.ok(response);
    }

}