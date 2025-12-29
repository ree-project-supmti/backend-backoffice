package com.ree.sireleves.dto.mobile;

import java.util.List;

public record MobileTourAddressDTO(
        Long addressId,
        String fullAddress,
        List<MobileTourCounterDetailDTO> counters
) {}
