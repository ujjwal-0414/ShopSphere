package com.ujjwal.ecommerce.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="order_items")
public class OrderItem {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    // bcz many items can belong to one order
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="order_id",nullable=false)
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="product_id",nullable=false)
    private Product product;

    @Column(nullable = false)
    private Integer quantity;

    // we have price here bcz when user order something and later on admin change the price of it but user should be able to see the price at which they purchased (historical snapshot)
    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal price;

    // to calculate quantity * price
    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal subtotal;

}
