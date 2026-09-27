package com.ujjwal.ecommerce.repository;

import com.ujjwal.ecommerce.entity.Product;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product,Long> {

    @EntityGraph(attributePaths = "category")
    List<Product> findByNameContainingIgnoreCase(String name);

    @EntityGraph(attributePaths = "category")
    List<Product> findByCategoryNameIgnoreCase(String categoryName);

    @EntityGraph(attributePaths = "category")
    List<Product> findByPriceBetween(BigDecimal minPrice,BigDecimal maxPrice);

    // we are using Pessimistic Lock for locking strategy
    // This will execute 'SELECT ... FOR UPDATE' to acquire an exclusive database lock on the Product row, preventing concurrent updates until the transaction completes.
    // it is used to prevent race conditions and data corruption in database when multiple users try to read and update the same piece of data at the same time.
    @Lock(LockModeType.PESSIMISTIC_WRITE) //PESSIMISTIC_WRITE means another transaction trying to acquire a conflicting lock on the same product must wait until this transaction finishes.
    @Query("SELECT p FROM Product p WHERE p.id = :id")
    Optional<Product> findByIdWithLock(@Param("id") Long id);

    @EntityGraph(attributePaths = "category")
    @Query("SELECT p FROM Product p")
    List<Product> findAllWithCategory();

    @EntityGraph(attributePaths = "category")
    @Query("SELECT p FROM Product p WHERE p.id = :id")
    Optional<Product> findByIdWithCategory(@Param("id") Long id);

}
