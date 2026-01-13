package com.ree.sireleves.controller.backoffice;

import com.ree.sireleves.dto.backoffice.AgentDTO;
import com.ree.sireleves.service.backoffice.AgentService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/backoffice")
public class AgentController {

    private final AgentService agentService;

    public AgentController(AgentService agentService) {
        this.agentService = agentService;
    }

    @GetMapping("/agents")
    public Page<AgentDTO> getAllAgents(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "firstName") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        return agentService.getAllActiveAgents(page, size, sortBy, sortDir);
    }

    @GetMapping("/agents/district/{district}")
    public Page<AgentDTO> getAgentsByDistrict(
            @PathVariable String district,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "firstName") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        return agentService.getAgentsByDistrict(district, page, size, sortBy, sortDir);
    }
}