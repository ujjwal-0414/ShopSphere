package com.ujjwal.ecommerce.repository;

import com.ujjwal.ecommerce.entity.Order;
import com.ujjwal.ecommerce.entity.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order,Long> {

    @EntityGraph(attributePaths = {"items", "items.product"})
    List<Order> findByUser(User user);

    @EntityGraph(attributePaths = {"items", "items.product"})
    Optional<Order> findById(Long id);

    Optional<Order> findByIdAndUser(Long orderId, User user);

    @EntityGraph(attributePaths = {"items", "items.product"})
    @Query("SELECT o FROM Order o")
    List<Order> findAllWithItemsAndProducts();

}
