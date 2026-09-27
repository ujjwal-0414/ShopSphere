package com.ujjwal.ecommerce.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false,length = 100)
    private String firstName;
    @Column(nullable = false,length = 100)
    private String lastName;

    @Column(nullable = false,unique = true,length = 255)
    private String email;

    @Column(nullable = false,length = 255)
    private String password;

    @Column(nullable = false)
    private String phoneNumber;

    @ManyToOne(fetch = FetchType.LAZY) // using lazy bcz loading everything every time would slow down the application
    @JoinColumn(name = "role_id",nullable = false)
    private Role role;

    @Column(nullable = false,updatable = false)
    private LocalDateTime createdAt;//using java.time to get time and date

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate(){ //protected bcz Keeps methods hidden from external layers, but open for Hibernate proxies and entity inheritance.
        this.updatedAt = LocalDateTime.now();
    }

}
