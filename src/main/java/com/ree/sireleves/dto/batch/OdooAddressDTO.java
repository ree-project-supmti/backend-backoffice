package com.ree.sireleves.dto.batch;

public record OdooAddressDTO(
        String street_number,
        String street,
        String apartment,
        String building,
        String residence,
        String district,
        String postal_code,
        String city
) {}
