package com.ujjwal.ecommerce.service;

import com.ujjwal.ecommerce.dto.request.AddToCartRequest;
import com.ujjwal.ecommerce.dto.request.UpdateCartItemRequest;
import com.ujjwal.ecommerce.dto.response.CartResponse;

public interface CartService {

    CartResponse addToCart(AddToCartRequest request);

    // we do not pass userId because the user is already authenticated through JWT
    CartResponse getCart();

    CartResponse updateCart(
            Long productId,
            UpdateCartItemRequest request
    );

    void removeCartItem(Long productId);
    void clearCart();

}
