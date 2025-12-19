package com.ree.sireleves.controller.dashboard;

import com.ree.sireleves.dto.dashboard.DashboardGlobalDTO;
import com.ree.sireleves.service.dashboard.DashboardService;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping("/district/{district}")
    public DashboardGlobalDTO byDistrict(
            @PathVariable String district,
            @RequestParam Instant start,
            @RequestParam Instant end
    ) {
        double coverage = dashboardService.coverageRateByDistrict(district, start, end);
        return new DashboardGlobalDTO(0, 0, coverage);
    }
}
