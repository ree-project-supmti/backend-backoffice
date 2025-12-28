package com.ree.sireleves.dto.dashboard;


import com.ree.sireleves.model.enums.CounterType;
import jakarta.validation.constraints.NotNull;

public record CounterCreateRequest(
        Long addressId,
        CounterType type
) {}

