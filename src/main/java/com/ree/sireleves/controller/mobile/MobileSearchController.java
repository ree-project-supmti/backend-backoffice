package com.ree.sireleves.controller.mobile;

import com.ree.sireleves.dto.mobile.MobileSearchResultDTO;
import com.ree.sireleves.service.mobile.MobileSearchService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/mobile/search")
@PreAuthorize("hasRole('AGENT')")
public class MobileSearchController {

    private final MobileSearchService searchService;

    public MobileSearchController(MobileSearchService searchService) {
        this.searchService = searchService;
    }

    @GetMapping
    public List<MobileSearchResultDTO> search(
            @RequestParam(required = false, defaultValue = "") String q,
            Authentication authentication
    ) {
        // Extract agent ID from JWT token (stored in subject)
        Long agentId = Long.parseLong(authentication.getName());
        
        return searchService.search(agentId, q);
    }
}
