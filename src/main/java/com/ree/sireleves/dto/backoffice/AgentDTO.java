package com.ree.sireleves.dto.backoffice;

public record AgentDTO(
        Long id,
        Long odooId,
        String firstName,
        String lastName,
        String phone,
        String district,
        Boolean active
) {
}