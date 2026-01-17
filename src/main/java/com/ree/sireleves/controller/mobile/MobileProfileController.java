package com.ree.sireleves.controller.mobile;

import com.ree.sireleves.dto.mobile.ChangeSecretCodeRequest;
import com.ree.sireleves.service.mobile.MobileAuthService;
import jakarta.security.auth.message.AuthException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/mobile/profile")
@PreAuthorize("hasRole('AGENT')")
public class MobileProfileController {

    private final MobileAuthService authService;

    public MobileProfileController(MobileAuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/change-password")
    public Map<String, String> changeSecretCode(
            @RequestBody ChangeSecretCodeRequest request,
            Authentication authentication
    ) throws AuthException {
        Long agentId = Long.parseLong(authentication.getName());
        authService.changeSecretCode(agentId, request.oldSecretCode(), request.newSecretCode());
        return Map.of("message", "Secret code changed successfully");
    }
}
