package com.ree.sireleves.dto.mobile;

public record MobileHomeAddressDTO(
        Long addressId,
        String fullAddress,
        String district,
        int totalCounters,
        int readCounters,
        boolean fullyRead
) {}
