package com.ree.sireleves.dto.batch;

import java.time.Instant;

public record FacturationReadingDTO(
        Long odooCounterId,
        Integer value,
        Instant readingDate
) {}
