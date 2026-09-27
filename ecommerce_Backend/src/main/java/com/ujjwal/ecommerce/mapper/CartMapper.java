package com.ujjwal.ecommerce.mapper;

import com.ujjwal.ecommerce.dto.response.CartItemResponse;
import com.ujjwal.ecommerce.dto.response.CartResponse;
import com.ujjwal.ecommerce.entity.Cart;
import com.ujjwal.ecommerce.entity.CartItem;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class CartMapper {

    public CartItemResponse toCartItemResponse(CartItem cartItem) {
        CartItemResponse response = new CartItemResponse();
        response.setId(cartItem.getId());

        response.setProductId(
                cartItem.getProduct().getId()
        );

        response.setProductName(
                cartItem.getProduct().getName()
        );

        response.setPrice(cartItem.getProduct().getPrice());
        response.setImageUrl(cartItem.getProduct().getImageUrl());
        response.setQuantity(cartItem.getQuantity());

        // Calculations belong in the service but here this particular calculation(price * quantity) is essentially response mapping/derived presentation data
        BigDecimal subTotal = cartItem.getProduct().getPrice().multiply(
                BigDecimal.valueOf(cartItem.getQuantity())
        );
        response.setSubtotal(subTotal);
        return response;
    }

    public CartResponse toCartResponse(Cart cart) {
        CartResponse response = new CartResponse();
        response.setId(cart.getId());
        response.setUserId(cart.getUser().getId());
        List<CartItemResponse> items = cart.getItems()
                .stream()
                .map(this::toCartItemResponse)
                .toList();

        response.setItems(items);
        BigDecimal totalPrice = items.stream()
                .map(CartItemResponse::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        response.setTotalPrice(totalPrice);
        response.setCreatedAt(cart.getCreatedAt());
        response.setUpdatedAt(cart.getUpdatedAt());
        return response;
    }

}
