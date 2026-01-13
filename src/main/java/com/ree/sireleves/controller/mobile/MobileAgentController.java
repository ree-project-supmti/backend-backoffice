package com.ree.sireleves.controller.mobile;

import com.ree.sireleves.dto.mobile.MobileAgentProfileDTO;
import com.ree.sireleves.service.mobile.MobileAgentService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/mobile/agents")
@PreAuthorize("hasRole('AGENT')")
public class MobileAgentController {

    private final MobileAgentService mobileAgentService;

    public MobileAgentController(MobileAgentService mobileAgentService) {
        this.mobileAgentService = mobileAgentService;
    }

    /**
     * Retrieves the profile information for the authenticated agent.
     * 
     * @param authentication The authentication object containing the agent ID
     * @return MobileAgentProfileDTO with agent details and statistics
     */
    @GetMapping("/me")
    public MobileAgentProfileDTO getProfile(Authentication authentication) {
        // Extract agent ID from authentication token
        Long agentId = Long.parseLong(authentication.getName());
        
        return mobileAgentService.getAgentProfile(agentId);
    }
}
