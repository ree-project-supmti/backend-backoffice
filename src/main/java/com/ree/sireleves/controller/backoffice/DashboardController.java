package com.ree.sireleves.controller.backoffice;

import com.ree.sireleves.dto.dashboard.DashboardGlobalDTO;
import com.ree.sireleves.service.dashboard.DashboardService;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;

@RestController
@RequestMapping("/api/backoffice")
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
        // Get real data from the service
        long totalCounters = dashboardService.getTotalCountersByDistrict(district);
        long totalReadings = dashboardService.getTotalReadingsByDistrict(district, start, end);
        double coverageRate = dashboardService.coverageRateByDistrict(district, start, end) / 100.0; // Convert percentage to decimal
        
        return new DashboardGlobalDTO(totalCounters, totalReadings, coverageRate);
    }
}
