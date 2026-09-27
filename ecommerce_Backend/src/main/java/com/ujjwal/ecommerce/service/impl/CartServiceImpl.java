package com.ujjwal.ecommerce.service.impl;

import com.ujjwal.ecommerce.dto.request.AddToCartRequest;
import com.ujjwal.ecommerce.dto.request.UpdateCartItemRequest;
import com.ujjwal.ecommerce.dto.response.CartResponse;
import com.ujjwal.ecommerce.entity.Cart;
import com.ujjwal.ecommerce.entity.CartItem;
import com.ujjwal.ecommerce.entity.Product;
import com.ujjwal.ecommerce.entity.User;
import com.ujjwal.ecommerce.exception.ResourceNotFoundException;
import com.ujjwal.ecommerce.exception.UnauthorizedException;
import com.ujjwal.ecommerce.mapper.CartMapper;
import com.ujjwal.ecommerce.repository.CartItemRepository;
import com.ujjwal.ecommerce.repository.CartRepository;
import com.ujjwal.ecommerce.repository.ProductRepository;
import com.ujjwal.ecommerce.security.CustomUserDetails;
import com.ujjwal.ecommerce.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartMapper cartMapper;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;

    // transaction is used to executes the complete add-to-cart operation as one database transaction.
    @Transactional
    @Override
    public CartResponse addToCart(AddToCartRequest request){
        User user = getAuthenticatedUser(); //because the user is already authenticated through JWT
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        Cart cart = getOrCreatecart(user);
        CartItem cartItem = cartItemRepository.findByCartAndProduct(cart,product).orElse(null);
        if(cartItem != null){
            cartItem.setQuantity(cartItem.getQuantity() + request.getQuantity());
        }
        else{ // it means there are no items in the cart yet
            cartItem =  new CartItem();
            cartItem.setCart(cart);
            cartItem.setProduct(product);
            cartItem.setQuantity(request.getQuantity());
            cart.getItems().add(cartItem);
        }
        cartItemRepository.save(cartItem);
        Cart savedCart = cartRepository.save(cart);
        return cartMapper.toCartResponse(savedCart);
    }

    @Transactional(readOnly = true) // this transaction is only going to read data
    @Override
    public CartResponse getCart(){
        User user = getAuthenticatedUser();
        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found"));
        return cartMapper.toCartResponse(cart);
    }

    @Transactional
    @Override
    public CartResponse updateCart(Long productId, UpdateCartItemRequest request){
        User user = getAuthenticatedUser();

        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found"));

        Product product = productRepository.findById(productId)
                .orElseThrow(()-> new ResourceNotFoundException("Product not found"));

        CartItem cartItem = cartItemRepository.findByCartAndProduct(cart,product)
                .orElseThrow(()-> new ResourceNotFoundException("Product is not in the cart"));

        cartItem.setQuantity(request.getQuantity());
        cartItemRepository.save(cartItem);
        Cart  savedCart = cartRepository.save(cart);
        return cartMapper.toCartResponse(savedCart);
    }

    @Transactional
    @Override
    public void removeCartItem(Long productId){
        User user = getAuthenticatedUser();

        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(()-> new ResourceNotFoundException("Cart not found"));

        Product product = productRepository.findById(productId)
                .orElseThrow(()-> new ResourceNotFoundException("Product not found"));

        CartItem cartItem = cartItemRepository.findByCartAndProduct(cart,product)
                .orElseThrow(()-> new ResourceNotFoundException("Product is not in the cart"));

        cart.getItems().remove(cartItem);
        cartItemRepository.delete(cartItem);
    }

    @Transactional
    @Override
    public void clearCart(){
        User user = getAuthenticatedUser();

        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(()-> new ResourceNotFoundException("Cart not found"));

        cart.getItems().clear();
        cartRepository.save(cart);
    }

    // not using transaction here bcz it is called in addTocart which has transactional annotation
    private Cart getOrCreatecart(User user){
        return cartRepository.findByUser(user)
                .orElseGet(()->{  // if cart is not there then create new cart
                    Cart cart = new Cart();
                    cart.setUser(user);
                    return cartRepository.save(cart);
                });
    }

    // it determines the user from the authentication context
    private User getAuthenticatedUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(authentication == null || !(authentication.getPrincipal() instanceof CustomUserDetails)){
            throw new UnauthorizedException(
                    "User is not authenticated"
            );
        }
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        return userDetails.getUser();
    }

}
