package com.ree.sireleves.dto.mobile;

import com.ree.sireleves.model.enums.CounterType;
import java.time.Instant;

public record MobileCounterDetailDTO(
        Long counterId,
        String serialNumber,
        CounterType type,
        Integer lastIndex,
        String fullAddress,
        String clientName,
        boolean readThisMonth,
        Instant lastReadingDate,
        Integer lastReadingValue
) {}
