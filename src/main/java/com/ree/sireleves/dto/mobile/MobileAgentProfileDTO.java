package com.ree.sireleves.dto.mobile;

public record MobileAgentProfileDTO(
        Long agentId,
        String firstName,
        String lastName,
        String phone,
        String district,
        int totalCountersInDistrict,
        int readingsThisMonth
) {}
