package com.ree.sireleves.dto.mobile;

import java.util.List;

public record MobileBatchReadingResponseDTO(
        int totalSubmitted,
        int successCount,
        int failureCount,
        List<ReadingErrorDTO> errors
) {}
