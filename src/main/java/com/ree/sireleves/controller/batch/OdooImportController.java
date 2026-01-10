package com.ree.sireleves.controller.batch;

import com.ree.sireleves.service.batch.OdooImportService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/batch/odoo")
public class OdooImportController {

    private final OdooImportService importService;

    public OdooImportController(OdooImportService importService) {
        this.importService = importService;
    }

    @PostMapping("/import-clients")
    public ResponseEntity<String> importClients() throws Exception {
        int count = importService.importClientsFromOdoo();
        return ResponseEntity.ok("✅ " + count + " clients importés depuis Odoo");
    }
}
