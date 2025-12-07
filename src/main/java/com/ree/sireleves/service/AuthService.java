// File: src/main/java/com/ree/sireleves/service/AuthService.java
package com.ree.sireleves.service;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import com.ree.sireleves.dto.AuthRequest;
import com.ree.sireleves.model.User;
import com.ree.sireleves.repository.UserRepository;
import java.time.Instant;
import org.springframework.security.crypto.password.PasswordEncoder;

@Service
public class AuthService {
    private final UserRepository userRepo;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepo, JwtService jwtService, PasswordEncoder passwordEncoder){
        this.userRepo = userRepo;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
    }

    public String authenticateAndGetToken(AuthRequest req){
        User user = userRepo.findByUsername(req.getUsername()).orElseThrow(()-> new RuntimeException("Invalid credentials"));
        if(!user.isEnabled()) throw new RuntimeException("User disabled");
        if(!passwordEncoder.matches(req.getPassword(), user.getPasswordHash())){
            throw new RuntimeException("Invalid credentials");
        }
        // generate JWT
        return jwtService.generateToken(user);
    }
}
