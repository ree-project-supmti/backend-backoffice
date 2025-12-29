package com.ree.sireleves.controller.mobile;

import com.ree.sireleves.dto.mobile.ChangeSecretCodeRequest;
import com.ree.sireleves.service.mobile.MobileAuthService;
import jakarta.security.auth.message.AuthException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/mobile/auth")
public class MobileAuthController {

    private final MobileAuthService authService;

    public MobileAuthController(MobileAuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public Map<String, String> login(@RequestParam String secretCode)
            throws AuthException {

        String token = authService.authenticate(secretCode);
        return Map.of("token", token);
    }

    @PostMapping("/change-password")
    @PreAuthorize("hasRole('AGENT')")
    public Map<String, String> changeSecretCode(
            @RequestBody ChangeSecretCodeRequest request,
            Authentication authentication
    ) throws AuthException {
        // Extract agent ID from authentication token
        Long agentId = Long.parseLong(authentication.getName());
        
        // Change the secret code
        authService.changeSecretCode(agentId, request.oldSecretCode(), request.newSecretCode());
        
        return Map.of("message", "Secret code changed successfully");
    }
}
