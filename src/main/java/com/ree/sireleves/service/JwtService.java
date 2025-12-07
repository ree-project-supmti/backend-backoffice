package com.ree.sireleves.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.stream.Collectors;

import com.ree.sireleves.model.User;

@Service
public class JwtService {

    private final SecretKey key;
    private final long expirationMillis;

    public JwtService(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.expiration-minutes}") long expirationMinutes
    ) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
        this.expirationMillis = expirationMinutes * 60 * 1000;
    }

    public String generateToken(User user) {

        long now = System.currentTimeMillis();
        String roles = user.getRoles().stream()
                .map(r -> r.getName())
                .collect(Collectors.joining(","));

        return Jwts.builder()
                .subject(user.getUuid())
                .claim("username", user.getUsername())
                .claim("roles", roles)
                .issuedAt(new Date(now))
                .expiration(new Date(now + expirationMillis))
                .signWith(key) // plus besoin de préciser HS256 (déduit depuis key)
                .compact();
    }

    public Jws<Claims> parseToken(String token) {
        return Jwts.parser()          // API JJWT 0.12.x
                .verifyWith(key)      // remplacement de setSigningKey(...)
                .build()
                .parseSignedClaims(token);
    }

    public long getExpirationMillis() {
        return expirationMillis;
    }
}
