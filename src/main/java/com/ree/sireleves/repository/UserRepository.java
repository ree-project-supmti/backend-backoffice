package com.ree.sireleves.repository;

// File: src/main/java/com/ree/sireleves/repository/UserRepository.java
import com.ree.sireleves.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByUuid(String uuid);
    boolean existsByUsername(String username);
}

