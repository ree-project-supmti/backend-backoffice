package com.ree.sireleves.repository;
// File: src/main/java/com/ree/sireleves/repository/PasswordResetTokenRepository.java
import com.ree.sireleves.model.PasswordResetToken;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {
    Optional<PasswordResetToken> findByToken(String token);
}
