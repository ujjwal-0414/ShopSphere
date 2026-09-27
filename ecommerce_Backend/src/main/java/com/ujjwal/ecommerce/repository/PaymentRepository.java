package com.ujjwal.ecommerce.repository;

import com.ujjwal.ecommerce.entity.Payment;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment,Long> {

    @EntityGraph(attributePaths = "order")
    Optional<Payment> findByOrderId(Long orderId);

    @EntityGraph(attributePaths = "order")
    Optional<Payment> findByTransactionId(String transactionId);

}
