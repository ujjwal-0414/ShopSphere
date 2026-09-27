package com.ujjwal.ecommerce.entity;

import com.ujjwal.ecommerce.enums.RoleType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="roles")
public class Role {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id; // long datatype to handle overflow

    @Enumerated(EnumType.STRING)
    @Column(nullable = false,unique = true)
    private RoleType name;

    @OneToMany(mappedBy = "role")
    private List<User> users;

}
