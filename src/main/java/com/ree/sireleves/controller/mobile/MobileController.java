package com.ree.sireleves.controller.mobile;

import com.ree.sireleves.dto.mobile.MobileReadingRequestDTO;
import com.ree.sireleves.model.Reading;
import com.ree.sireleves.service.mobile.MobileReadingService;
import com.ree.sireleves.service.mobile.MobileTourService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/mobile")
@PreAuthorize("hasRole('AGENT')")
public class MobileController {

    private final MobileTourService tourService;
    private final MobileReadingService readingService;

    public MobileController(
            MobileTourService tourService,
            MobileReadingService readingService
    ) {
        this.tourService = tourService;
        this.readingService = readingService;
    }

    @GetMapping("/agents/{agentId}/tournee")
    public List<?> getTournee(@PathVariable Long agentId) {
        return tourService.getTourneeForAgent(agentId);
    }

    @PostMapping("/readings")
    public Reading submitReading(@RequestBody MobileReadingRequestDTO dto) {
        Reading reading = new Reading();
        reading.setMobileUuid(dto.mobileUuid());
        reading.setValue(dto.value());
        reading.setReadingDate(dto.readingDate());
        reading.setLatitude(dto.latitude());
        reading.setLongitude(dto.longitude());
        return readingService.submitReading(reading);
    }
}
