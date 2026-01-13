package com.ree.sireleves.controller.batch;

import com.ree.sireleves.service.DataSeedingService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/batch/seed")
public class DataSeedController {

    private final DataSeedingService dataSeedingService;

    public DataSeedController(DataSeedingService dataSeedingService) {
        this.dataSeedingService = dataSeedingService;
    }

    @PostMapping("/test-data")
    @PreAuthorize("hasRole('SUPERADMIN')")
    public ResponseEntity<Map<String, String>> seedTestData() {
        try {
            dataSeedingService.seedTestData();
            return ResponseEntity.ok(Map.of(
                "status", "success",
                "message", "Test data seeded successfully for API testing"
            ));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of(
                "status", "error",
                "message", "Failed to seed test data: " + e.getMessage()
            ));
        }
    }

    @DeleteMapping("/clear-all")
    @PreAuthorize("hasRole('SUPERADMIN')")
    public ResponseEntity<Map<String, String>> clearAllData() {
        try {
            dataSeedingService.clearAllData();
            return ResponseEntity.ok(Map.of(
                "status", "success",
                "message", "All data cleared successfully"
            ));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of(
                "status", "error",
                "message", "Failed to clear data: " + e.getMessage()
            ));
        }
    }
}