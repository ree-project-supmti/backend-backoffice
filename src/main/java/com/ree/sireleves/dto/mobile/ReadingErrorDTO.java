package com.ree.sireleves.dto.mobile;

public record ReadingErrorDTO(
        String mobileUuid,
        Long counterId,
        String errorMessage
) {}
