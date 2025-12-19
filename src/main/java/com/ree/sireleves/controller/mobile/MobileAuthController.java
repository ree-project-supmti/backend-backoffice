package com.ree.sireleves.controller.mobile;

import com.ree.sireleves.service.mobile.MobileAuthService;
import jakarta.security.auth.message.AuthException;
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



}
