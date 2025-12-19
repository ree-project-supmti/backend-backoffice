package com.ree.sireleves.dto.batch;

public record OdooClientDTO(
        Long odooId,
        String name,
        String phone,
        String email,
        String district
) {}
