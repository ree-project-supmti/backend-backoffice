// File: src/main/java/com/ree/sireleves/controller/AuthController.java
package com.ree.sireleves.controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import com.ree.sireleves.service.AuthService;
import com.ree.sireleves.dto.AuthRequest;
import com.ree.sireleves.dto.AuthResponse;
import com.ree.sireleves.service.JwtService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;
    private final JwtService jwtService;

    public AuthController(AuthService authService, JwtService jwtService){
        this.authService = authService;
        this.jwtService = jwtService;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest req){
        String token = authService.authenticateAndGetToken(req);
        return ResponseEntity.ok(new AuthResponse(token, jwtService.getExpirationMillis()/1000));
    }
}
