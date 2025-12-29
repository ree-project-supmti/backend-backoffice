package com.ree.sireleves.service.mobile;

import com.ree.sireleves.model.Agent;
import com.ree.sireleves.repository.AgentRepository;
import com.ree.sireleves.service.JwtService;
import jakarta.security.auth.message.AuthException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;

@Service
public class MobileAuthService {

    private static final Duration MOBILE_JWT_TTL = Duration.ofMinutes(10);

    private final AgentRepository agentRepository;
    private final JwtService jwtService;

    public MobileAuthService(
            AgentRepository agentRepository,
            JwtService jwtService
    ) {
        this.agentRepository = agentRepository;
        this.jwtService = jwtService;
    }

    public String authenticate(String secretCode) throws AuthException {

        if (!secretCode.matches("\\d{6}")) {
            throw new AuthException("Invalid secret code format");
        }

        Agent agent = agentRepository.findBySecretCode(secretCode)
                .orElseThrow(() -> new AuthException("Invalid secret code"));

        if (!agent.getActive()) {
            throw new AuthException("Agent disabled");
        }

        return jwtService.generateMobileToken(
                agent.getId().toString(),
                MOBILE_JWT_TTL
        );

    }

    @Transactional
    public void changeSecretCode(Long agentId, String oldSecretCode, String newSecretCode) throws AuthException {
        // Retrieve the agent
        Agent agent = agentRepository.findById(agentId)
                .orElseThrow(() -> new IllegalArgumentException("Agent not found"));

        // Validate old secret code matches current
        if (!agent.getSecretCode().equals(oldSecretCode)) {
            throw new IllegalArgumentException("Old secret code is incorrect");
        }

        // Validate new secret code is 6 digits
        if (!newSecretCode.matches("\\d{6}")) {
            throw new IllegalArgumentException("New secret code must be exactly 6 digits");
        }

        // Update agent's secret code
        agent.setSecretCode(newSecretCode);
        agentRepository.save(agent);
    }
}
