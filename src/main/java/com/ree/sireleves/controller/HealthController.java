package com.ree.sireleves.controller;

import com.ree.sireleves.service.DataSeedingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class HealthController {

    private final DataSeedingService dataSeedingService;

    public HealthController(DataSeedingService dataSeedingService) {
        this.dataSeedingService = dataSeedingService;
    }

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

    @PostMapping("/seed-test-data")
    public ResponseEntity<Map<String, Object>> seedTestData() {
        try {
            dataSeedingService.seedTestData();
            return ResponseEntity.ok(Map.of(
                "status", "SUCCESS",
                "message", "Test data seeded successfully in MySQL database",
                "timestamp", LocalDateTime.now()
            ));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of(
                "status", "ERROR",
                "message", "Failed to seed test data: " + e.getMessage(),
                "timestamp", LocalDateTime.now()
            ));
        }
    }

    @PostMapping("/clear-test-data")
    public ResponseEntity<Map<String, Object>> clearTestData() {
        try {
            dataSeedingService.clearAllData();
            return ResponseEntity.ok(Map.of(
                "status", "SUCCESS",
                "message", "Test data cleared from MySQL database",
                "timestamp", LocalDateTime.now()
            ));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of(
                "status", "ERROR",
                "message", "Failed to clear test data: " + e.getMessage(),
                "timestamp", LocalDateTime.now()
            ));
        }
    }
}