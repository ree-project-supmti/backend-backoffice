package com.ree.sireleves.dto.mobile;

import java.time.Instant;

public record MobileReadingRequestDTO(
        Long counterId,
        Long agentId,
        Integer value,
        Instant readingDate,
        Double latitude,
        Double longitude,
        String photoBase64,
        String mobileUuid
) {}
