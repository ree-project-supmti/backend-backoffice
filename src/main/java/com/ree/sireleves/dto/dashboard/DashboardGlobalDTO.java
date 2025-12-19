package com.ree.sireleves.dto.dashboard;

public record DashboardGlobalDTO(
        long totalCounters,
        long totalReadings,
        double coverageRate
) {}
