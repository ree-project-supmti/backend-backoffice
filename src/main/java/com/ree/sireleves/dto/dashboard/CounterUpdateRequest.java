package com.ree.sireleves.dto.dashboard;

import jakarta.validation.constraints.NotNull;

public record CounterUpdateRequest(
        @NotNull Boolean active
) {}
