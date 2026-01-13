package com.ree.sireleves.dto.batch;

import java.util.List;

public record OdooClientDTO(
        Long odoo_id,
        String first_name,
        String last_name,
        String phone,
        String email,
        List<OdooAddressDTO> addresses
) {}
