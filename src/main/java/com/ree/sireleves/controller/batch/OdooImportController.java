package com.ree.sireleves.controller.batch;

import com.ree.sireleves.service.batch.OdooAgentImportService;
import com.ree.sireleves.service.batch.OdooImportService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/batch/odoo")
public class OdooImportController {

    private final OdooImportService clientImportService;
    private final OdooAgentImportService agentImportService;

    public OdooImportController(OdooImportService clientImportService,
                                OdooAgentImportService agentImportService) {
        this.clientImportService = clientImportService;
        this.agentImportService = agentImportService;
    }

    // -------- CLIENTS --------
    @PostMapping("/import-clients")
    public ResponseEntity<String> importClients() throws Exception {
        int count = clientImportService.importClientsFromOdoo();
        return ResponseEntity.ok("✅ " + count + " clients importés depuis Odoo");
    }

    // -------- AGENTS --------
    @PostMapping("/import-agents")
    public ResponseEntity<String> importAgents() throws Exception {
        int count = agentImportService.importAgentsFromOdoo();
        return ResponseEntity.ok("✅ " + count + " agents importés depuis Odoo RH");
    }
}

