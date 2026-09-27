package com.ujjwal.ecommerce.repository;

import com.ujjwal.ecommerce.entity.Order;
import com.ujjwal.ecommerce.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem,Long> {

    List<OrderItem> findByOrder(Order order);

}
