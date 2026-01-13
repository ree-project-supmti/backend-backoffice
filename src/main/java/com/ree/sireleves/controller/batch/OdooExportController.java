package com.ree.sireleves.controller.batch;

import com.ree.sireleves.service.batch.OdooExportService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/batch/odoo")
public class OdooExportController {

    private final OdooExportService exportService;

    public OdooExportController(OdooExportService exportService) {
        this.exportService = exportService;
    }

    @PostMapping("/export-readings")
    public ResponseEntity<String> exportReadings() throws Exception {
        int count = exportService.exportValidatedReadingsToOdoo();
        return ResponseEntity.ok("✅ " + count + " relevés exportés vers Odoo");
    }
}

