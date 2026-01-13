package com.ree.sireleves.dto.mobile;

import java.util.List;

public record MobileTourDownloadDTO(
        String district,
        int totalCounters,
        int unreadCounters,
        List<MobileTourAddressDTO> addresses
) {}
