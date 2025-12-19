package com.ree.sireleves.dto.mobile;

import com.ree.sireleves.model.enums.CounterType;

public record MobileHomeCounterDTO(
        Long counterId,
        String serialNumber,
        CounterType type,
        String address,
        boolean read
) {}
