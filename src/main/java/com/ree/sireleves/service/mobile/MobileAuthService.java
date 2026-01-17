package com.ree.sireleves.service.mobile;

import com.ree.sireleves.model.Agent;
import com.ree.sireleves.repository.AgentRepository;
import com.ree.sireleves.service.JwtService;
import jakarta.security.auth.message.AuthException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;

@Service
public class MobileAuthService {

    private static final Logger logger = LoggerFactory.getLogger(MobileAuthService.class);
    private static final Duration MOBILE_JWT_TTL = Duration.ofMinutes(10);

    private final AgentRepository agentRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    public MobileAuthService(
            AgentRepository agentRepository,
            JwtService jwtService,
            PasswordEncoder passwordEncoder
    ) {
        this.agentRepository = agentRepository;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
    }

    public String authenticate(String secretCode) throws AuthException {
        logger.info("Secret code: " + secretCode);
        logger.info("Secret code length: " + secretCode.length());
        
        
        // Validate input is not null or empty
        if (secretCode == null || secretCode.trim().isEmpty()) {
            logger.warn("Authentication attempt with null or empty secret code");
            throw new AuthException("Le code secret est requis.");
        }

        // Clean the secret code by removing whitespace and non-digit characters
        String cleanedSecretCode = secretCode.replaceAll("\\s+", "").replaceAll("\\D", "");

        // Validate cleaned secret code is exactly 6 digits
        if (!cleanedSecretCode.matches("\\d{6}")) {
            logger.warn("Authentication attempt with invalid secret code format. Original length: {}, Cleaned length: {}",
                       secretCode.length(), cleanedSecretCode.length());
            throw new AuthException("Le code secret doit contenir exactement 6 chiffres.");
        }

        Agent agent = agentRepository.findByActiveTrue().stream()
                .filter(a -> passwordEncoder.matches(cleanedSecretCode, a.getSecretCode()))
                .findFirst()
                .orElseThrow(() -> {
                    logger.warn("Authentication failed: Invalid secret code provided");
                    return new AuthException("Code secret incorrect. Veuillez réessayer.");
                });

        logger.info("Successful authentication for agent ID: {}", agent.getId());
        return jwtService.generateMobileToken(
                agent.getId().toString(),
                MOBILE_JWT_TTL
        );

    }

    @Transactional
    public void changeSecretCode(Long agentId, String oldSecretCode, String newSecretCode) throws AuthException {
        // Retrieve the agent
        Agent agent = agentRepository.findById(agentId)
                .orElseThrow(() -> new IllegalArgumentException("Agent non trouvé."));

        // Clean and validate old secret code
        if (oldSecretCode == null || oldSecretCode.trim().isEmpty()) {
            throw new AuthException("L'ancien code secret est requis.");
        }
        String cleanedOldSecretCode = oldSecretCode.replaceAll("\\s+", "").replaceAll("\\D", "");

        // Validate old secret code matches current (compare hashed)
        if (!passwordEncoder.matches(cleanedOldSecretCode, agent.getSecretCode())) {
            throw new AuthException("L'ancien code secret est incorrect.");
        }

        // Clean and validate new secret code
        if (newSecretCode == null || newSecretCode.trim().isEmpty()) {
            throw new IllegalArgumentException("Le nouveau code secret est requis.");
        }
        String cleanedNewSecretCode = newSecretCode.replaceAll("\\s+", "").replaceAll("\\D", "");

        // Validate new secret code is exactly 6 digits
        if (!cleanedNewSecretCode.matches("\\d{6}")) {
            throw new IllegalArgumentException("Le nouveau code secret doit contenir exactement 6 chiffres.");
        }

        // Hash and update agent's secret code
        String hashedSecretCode = passwordEncoder.encode(cleanedNewSecretCode);
        agent.setSecretCode(hashedSecretCode);
        agentRepository.save(agent);
    }
}
