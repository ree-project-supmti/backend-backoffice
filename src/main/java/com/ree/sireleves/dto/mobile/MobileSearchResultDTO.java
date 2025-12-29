package com.ree.sireleves.dto.mobile;

import com.ree.sireleves.model.enums.CounterType;

public record MobileSearchResultDTO(
        Long counterId,
        String serialNumber,
        CounterType type,
        String address,
        String matchType,
        boolean read
) {}
