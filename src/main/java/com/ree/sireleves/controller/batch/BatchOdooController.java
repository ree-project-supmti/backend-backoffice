package com.ree.sireleves.controller.batch;

import com.ree.sireleves.dto.batch.OdooClientDTO;
import com.ree.sireleves.service.batch.OdooImportService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/batch/odoo")
@PreAuthorize("hasRole('SUPERADMIN')")
public class BatchOdooController {

    private final OdooImportService importService;

    public BatchOdooController(OdooImportService importService) {
        this.importService = importService;
    }

    @PostMapping("/clients")
    public void importClients(@RequestBody List<OdooClientDTO> clients) {
        // mapping DTO → Entity à ajouter
    }
}
