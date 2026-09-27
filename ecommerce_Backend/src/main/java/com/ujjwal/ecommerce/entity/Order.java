package com.ujjwal.ecommerce.entity;

import com.ujjwal.ecommerce.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // it is bcz many orders can belong to one user
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // we are using are enums to directly and safely save pending,confirmed,etc in database
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status;

    // to store final total
    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal totalAmount;

    // we do not used @ManyToOne Address address bcz then the historical order depends on the mutable Address record.
    @Column(nullable = false, length = 100)
    private String shippingFullName;

    @Column(nullable = false, length = 20)
    private String shippingPhoneNumber;

    @Column(nullable = false, length = 255)
    private String shippingAddressLine;

    @Column(nullable = false, length = 100)
    private String shippingCity;

    @Column(nullable = false, length = 100)
    private String shippingState;

    @Column(nullable = false, length = 100)
    private String shippingCountry;

    @Column(nullable = false, length = 20)
    private String shippingPostalCode;

    // one to many bcz one order can contain many items
    @OneToMany(
            mappedBy = "order",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<OrderItem> items = new ArrayList<>();

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {

        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();

        if (this.status == null) {
            this.status = OrderStatus.PENDING;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}