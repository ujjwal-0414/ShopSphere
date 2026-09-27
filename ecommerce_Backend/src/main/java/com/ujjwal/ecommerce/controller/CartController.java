package com.ujjwal.ecommerce.controller;

import com.ujjwal.ecommerce.dto.request.AddToCartRequest;
import com.ujjwal.ecommerce.dto.request.UpdateCartItemRequest;
import com.ujjwal.ecommerce.dto.response.CartResponse;
import com.ujjwal.ecommerce.service.CartService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
@Validated
public class CartController {

    private final CartService cartService;

    @PostMapping("/add")
    public ResponseEntity<CartResponse> addToCart(@Valid @RequestBody AddToCartRequest request) {
        CartResponse response = cartService.addToCart(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping("/all")
    public ResponseEntity<CartResponse> getCart() {

        CartResponse response =
                cartService.getCart();

        return ResponseEntity.ok(response);
    }

    @PutMapping("/items/{productId}")
    public ResponseEntity<CartResponse> updateCartItem(
            @PathVariable @Positive(message = "Product ID must be positive") Long productId,
            @Valid @RequestBody UpdateCartItemRequest request
    ) {

        CartResponse response =
                cartService.updateCart(
                        productId,
                        request
                );

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/items/{productId}")
    public ResponseEntity<Void> removeCartItem(
            @PathVariable @Positive(message = "Product ID must be positive") Long productId
    ) {

        cartService.removeCartItem(productId);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> clearCart() {

        cartService.clearCart();

        return ResponseEntity.noContent().build();
    }

}
