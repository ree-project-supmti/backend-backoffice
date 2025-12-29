package com.ree.sireleves.service.mobile;

import com.ree.sireleves.model.Agent;
import com.ree.sireleves.repository.AgentRepository;
import com.ree.sireleves.service.JwtService;
import jakarta.security.auth.message.AuthException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;

@Service
public class MobileAuthService {

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

        if (!secretCode.matches("\\d{6}")) {
            throw new AuthException("Invalid secret code format");
        }

        Agent agent = agentRepository.findByActiveTrue().stream()
                .filter(a -> passwordEncoder.matches(secretCode, a.getSecretCode()))
                .findFirst()
                .orElseThrow(() -> new AuthException("Invalid secret code"));

        return jwtService.generateMobileToken(
                agent.getId().toString(),
                MOBILE_JWT_TTL
        );

    }

    @Transactional
    public void changeSecretCode(Long agentId, String oldSecretCode, String newSecretCode) throws AuthException {
        // Retrieve the agent
        Agent agent = agentRepository.findById(agentId)
                .orElseThrow(() -> new AuthException("Agent not found"));

        // Validate old secret code matches current (compare hashed)
        if (!passwordEncoder.matches(oldSecretCode, agent.getSecretCode())) {
            throw new AuthException("Old secret code is incorrect");
        }

        // Validate new secret code is 6 digits
        if (!newSecretCode.matches("\\d{6}")) {
            throw new AuthException("New secret code must be exactly 6 digits");
        }

        // Hash and update agent's secret code
        String hashedSecretCode = passwordEncoder.encode(newSecretCode);
        agent.setSecretCode(hashedSecretCode);
        agentRepository.save(agent);
    }
}
