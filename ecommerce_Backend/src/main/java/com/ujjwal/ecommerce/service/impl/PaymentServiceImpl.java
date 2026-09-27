package com.ujjwal.ecommerce.service.impl;

import com.ujjwal.ecommerce.dto.request.PaymentRequest;
import com.ujjwal.ecommerce.dto.response.PaymentResponse;
import com.ujjwal.ecommerce.exception.ResourceNotFoundException;
import com.ujjwal.ecommerce.exception.BadRequestException;
import com.ujjwal.ecommerce.exception.ForbiddenException;
import com.ujjwal.ecommerce.exception.ConflictException;
import com.ujjwal.ecommerce.exception.UnauthorizedException;
import com.ujjwal.ecommerce.entity.Order;
import com.ujjwal.ecommerce.entity.Payment;
import com.ujjwal.ecommerce.entity.User;
import com.ujjwal.ecommerce.enums.OrderStatus;
import com.ujjwal.ecommerce.enums.PaymentStatus;
import com.ujjwal.ecommerce.mapper.PaymentMapper;
import com.ujjwal.ecommerce.repository.OrderRepository;
import com.ujjwal.ecommerce.repository.PaymentRepository;
import com.ujjwal.ecommerce.security.CustomUserDetails;
import com.ujjwal.ecommerce.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final OrderRepository orderRepository;
    private final PaymentRepository paymentRepository;
    private final PaymentMapper paymentMapper;

    @Override
    @Transactional
    public PaymentResponse createPayment(PaymentRequest request) {
        User user = getAuthenticatedUser();

        Order order = orderRepository.findById(request.getOrderId())
                .orElseThrow(() -> new ResourceNotFoundException("Order Not Found"));

        if(!order.getUser().getId().equals(user.getId())){
            throw new ForbiddenException("You are not authorized to make payment for this order");
        }

        if (order.getStatus() != OrderStatus.PENDING) {
            throw new BadRequestException(
                    "Payment can only be made for a pending order");
        }

        if (order.getTotalAmount() == null ||
                order.getTotalAmount().signum() <= 0) {
            throw new BadRequestException(
                    "Invalid order amount");
        }

        if(paymentRepository.findByOrderId(order.getId()).isPresent()){
            throw new ConflictException("Payment already exists for this order");
        }

        Payment payment = new Payment();
        payment.setOrder(order);
        payment.setAmount(order.getTotalAmount());
        payment.setPaymentMethod(request.getPaymentMethod());
        // Simulated payment is immediately successful
        payment.setStatus(PaymentStatus.SUCCESS);
        payment.setTransactionId("TXN-" + UUID.randomUUID().toString());

        Payment savedPayment = paymentRepository.save(payment);
        // Payment succeeded, so confirm the order
        order.setStatus(OrderStatus.CONFIRMED);
        orderRepository.save(order);
        return paymentMapper.toPaymentResponse(savedPayment);
    }

    @Override
    @Transactional(readOnly = true)
    public PaymentResponse getPaymentByOrderId(Long orderId) {
        User user = getAuthenticatedUser();

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Order not found"));

        if (!order.getUser().getId().equals(user.getId())) {
            throw new ForbiddenException(
                    "You are not authorized to access this payment");
        }

        Payment payment = paymentRepository.findByOrderId(orderId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Payment not found for this order"));

        return paymentMapper.toPaymentResponse(payment);
    }

    private User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(authentication == null || !(authentication.getPrincipal() instanceof CustomUserDetails)){
            throw new UnauthorizedException(
                    "Authenticated user not found"
            );
        }
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        return userDetails.getUser();
    }

}
