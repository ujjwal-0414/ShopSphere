package com.ujjwal.ecommerce.repository;

import com.ujjwal.ecommerce.entity.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {

    // it tells Spring data jpa,when finding a User by email, fetch the role relationship as well so now instead of user + lazy it will give user + role already loaded
    @EntityGraph(attributePaths = "role")
    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

}
