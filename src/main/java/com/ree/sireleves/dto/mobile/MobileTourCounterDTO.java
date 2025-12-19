package com.ree.sireleves.dto.mobile;

public record MobileTourCounterDTO(
        Long counterId,
        String serialNumber,
        String clientName,
        String district,
        String address
) {}
