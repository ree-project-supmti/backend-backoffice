package com.ree.sireleves.dto.mobile;

import com.ree.sireleves.model.enums.CounterType;

public record MobileTourCounterDetailDTO(
        Long counterId,
        String serialNumber,
        CounterType type,
        Integer lastIndex,
        String clientName
) {}
