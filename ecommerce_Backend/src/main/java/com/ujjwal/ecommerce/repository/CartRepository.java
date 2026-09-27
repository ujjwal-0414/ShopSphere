package com.ujjwal.ecommerce.repository;

import com.ujjwal.ecommerce.entity.Cart;
import com.ujjwal.ecommerce.entity.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {

    // it tells JPA that when finding a user's cart, also fetch everything required to build CartResponse
    @EntityGraph(attributePaths = {
            "user",
            "items",
            "items.product"
    })
    Optional<Cart> findByUser(User user);

}
