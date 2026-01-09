package com.ree.sireleves.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class HealthController {

    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> health() {
        return ResponseEntity.ok(Map.of(
            "status", "UP",
            "timestamp", LocalDateTime.now(),
            "service", "REE Mobile API",
            "version", "1.0.0"
        ));
    }

    @GetMapping("/mobile/health")
    public ResponseEntity<Map<String, Object>> mobileHealth() {
        return ResponseEntity.ok(Map.of(
            "status", "UP",
            "timestamp", LocalDateTime.now(),
            "service", "REE Mobile API - Mobile Endpoints",
            "version", "1.0.0",
            "endpoints", Map.of(
                "auth", "/api/mobile/auth/login",
                "download", "/api/mobile/agents/{agentId}/download",
                "readings", "/api/mobile/readings"
            )
        ));
    }
}