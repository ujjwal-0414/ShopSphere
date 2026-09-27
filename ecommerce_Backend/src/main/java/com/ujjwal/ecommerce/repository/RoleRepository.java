package com.ujjwal.ecommerce.repository;

import com.ujjwal.ecommerce.entity.Role;
import com.ujjwal.ecommerce.enums.RoleType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role,Long> {

    Optional<Role> findByName(RoleType name);

}
