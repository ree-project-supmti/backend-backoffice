package com.ree.sireleves.dto.mobile;

import java.time.Instant;

public record MobileReadingHistoryDTO(
        Long readingId,
        Integer indexValue,
        Instant readingDate,
        Integer consumption,
        String agentName
) {}
