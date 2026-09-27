package com.ujjwal.ecommerce.repository;

import com.ujjwal.ecommerce.entity.Cart;
import com.ujjwal.ecommerce.entity.CartItem;
import com.ujjwal.ecommerce.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    Optional<CartItem> findByCartAndProduct(
            Cart cart,
            Product product
    );

}
