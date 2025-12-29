package com.ree.sireleves.controller.mobile;

import com.ree.sireleves.dto.mobile.MobileReadingHistoryDTO;
import com.ree.sireleves.service.mobile.MobileCounterService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/mobile/counters")
@PreAuthorize("hasRole('AGENT')")
public class MobileCounterController {

    private final MobileCounterService counterService;

    public MobileCounterController(MobileCounterService counterService) {
        this.counterService = counterService;
    }

    /**
     * Get reading history for a counter.
     * Returns last 12 readings ordered by date descending with consumption calculated.
     */
    @GetMapping("/{counterId}/history")
    public List<MobileReadingHistoryDTO> getReadingHistory(
            @PathVariable Long counterId,
            Authentication authentication
    ) {
        // Extract agent ID from authentication token
        Long authenticatedAgentId = Long.parseLong(authentication.getName());
        
        return counterService.getReadingHistory(counterId, authenticatedAgentId);
    }
}
