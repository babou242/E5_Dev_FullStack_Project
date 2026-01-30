package com.example.TP3_Pinia.repository;
import com.example.TP3_Pinia.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);
    boolean existsByUsername ( String username );
}