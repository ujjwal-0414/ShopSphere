package com.ujjwal.ecommerce.service.impl;

import com.ujjwal.ecommerce.dto.request.PlaceOrderRequest;
import com.ujjwal.ecommerce.dto.response.OrderResponse;
import com.ujjwal.ecommerce.enums.PaymentStatus;
import com.ujjwal.ecommerce.exception.BadRequestException;
import com.ujjwal.ecommerce.exception.ResourceNotFoundException;
import com.ujjwal.ecommerce.exception.ForbiddenException;
import com.ujjwal.ecommerce.exception.UnauthorizedException;
import com.ujjwal.ecommerce.entity.*;
import com.ujjwal.ecommerce.enums.OrderStatus;
import com.ujjwal.ecommerce.mapper.OrderMapper;
import com.ujjwal.ecommerce.repository.*;
import com.ujjwal.ecommerce.security.CustomUserDetails;
import com.ujjwal.ecommerce.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final AddressRepository addressRepository;
    private final PaymentRepository paymentRepository;
    private final OrderMapper orderMapper;

    @Override
    @Transactional
    public OrderResponse placeOrder(PlaceOrderRequest request){
        // Gets authenticated user
        User user  = getAuthenticatedUser();

        // checks that the selected address belonging to current user or not
        Address address = addressRepository
                .findByIdAndUser(request.getAddressId(), user)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Address not found"));

        // Find user's cart
        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found"));

        if(cart.getItems() == null || cart.getItems().isEmpty()){
            throw new BadRequestException("Cart is empty");
        }

        Order order = new Order();
        order.setUser(user);
        order.setStatus(OrderStatus.PENDING);

        order.setShippingFullName(
                address.getFullName()
        );

        order.setShippingPhoneNumber(
                address.getPhoneNumber()
        );

        order.setShippingAddressLine(
                address.getAddressLine()
        );

        order.setShippingCity(
                address.getCity()
        );

        order.setShippingState(
                address.getState()
        );

        order.setShippingCountry(
                address.getCountry()
        );

        order.setShippingPostalCode(
                address.getPostalCode()
        );

        BigDecimal totalAmount = BigDecimal.ZERO;

        for(CartItem cartItem:cart.getItems()){

            //Fetch product with PESSIMISTIC_WRITE lock instead of cartItem.getProduct()
            //we have removed the unlocked pre-check loop
            Product product = productRepository
                    .findByIdWithLock(cartItem.getProduct().getId())
                    .orElseThrow(() ->
                            new ResourceNotFoundException("Product not found"));

            //Perform stock validation under active DB lock
            if (product.getStock() < cartItem.getQuantity()) {
                throw new BadRequestException(
                        "Insufficient stock for product: "
                                + product.getName()
                );
            }

            BigDecimal price = product.getPrice();
            BigDecimal subTotal = price.multiply(BigDecimal.valueOf(cartItem.getQuantity()));

            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(product);
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPrice(price);
            orderItem.setSubtotal(subTotal);

            order.getItems().add(orderItem);

            // Deducting the purchased quantity from the available product stock.
            product.setStock(product.getStock() - cartItem.getQuantity());

            totalAmount = totalAmount.add(subTotal);
        }

        order.setTotalAmount(totalAmount);
        Order savedOrder = orderRepository.save(order);
        cart.getItems().clear();
        return orderMapper.toOrderResponse(savedOrder);
    }

    @Override
    @Transactional(readOnly = true) // this transaction is only going to read data
    public List<OrderResponse> getMyOrders(){
        User user = getAuthenticatedUser();

        return orderRepository.findByUser(user)
                .stream()
                .map(orderMapper::toOrderResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public OrderResponse getOrderById(Long orderId) {
        User user = getAuthenticatedUser();

        Order order = orderRepository.findByIdAndUser(orderId, user)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Order not found"));

        return orderMapper.toOrderResponse(order);
    }

    @Override
    @Transactional
    public OrderResponse cancelOrder(Long orderId) {

        User user = getAuthenticatedUser();

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        if (!order.getUser().getId().equals(user.getId())) {
            throw new ForbiddenException(
                    "You are not allowed to cancel this order"
            );
        }

        if (order.getStatus() != OrderStatus.PENDING &&
                order.getStatus() != OrderStatus.CONFIRMED) {

            throw new BadRequestException(
                    "Order cannot be cancelled in its current status :" + order.getStatus()
            );
        }

        for (OrderItem orderItem : order.getItems()) {

            //Fetch product with PESSIMISTIC_WRITE lock before restoring inventory count
            Product product = productRepository
                    .findByIdWithLock(orderItem.getProduct().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

            product.setStock(product.getStock() + orderItem.getQuantity());
        }

        order.setStatus(OrderStatus.CANCELLED);

        // If the order was already paid successfully,
        // mark the simulated payment as refunded.
        paymentRepository.findByOrderId(order.getId())
                .ifPresent(payment -> {
                    if (payment.getStatus() == PaymentStatus.SUCCESS) {
                        payment.setStatus(PaymentStatus.REFUNDED);
                        paymentRepository.save(payment);
                    }
                });

        Order savedOrder = orderRepository.save(order);

        return orderMapper.toOrderResponse(savedOrder);
    }

    // it determines that the user is authenticated or not
    private User  getAuthenticatedUser(){

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
