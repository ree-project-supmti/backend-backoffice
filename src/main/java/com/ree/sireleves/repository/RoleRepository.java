package com.ree.sireleves.repository;

// File: src/main/java/com/ree/sireleves/repository/RoleRepository.java
import com.ree.sireleves.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(String name);
}

