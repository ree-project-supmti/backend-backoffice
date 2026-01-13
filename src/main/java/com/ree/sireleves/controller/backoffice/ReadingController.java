package com.ree.sireleves.controller.backoffice;

import com.ree.sireleves.model.Reading;
import com.ree.sireleves.service.backoffice.ReadingService;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/backoffice/readings")
@PreAuthorize("hasRole('USER')")
public class ReadingController {

    private final ReadingService readingService;

    public ReadingController(ReadingService readingService) {
        this.readingService = readingService;
    }

    @GetMapping
    public Page<Reading> getAllReadings(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "readingDate") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        return readingService.getAllReadings(page, size, sortBy, sortDir);
    }

    @GetMapping("/district/{district}")
    public Page<Reading> getReadingsByDistrict(
            @PathVariable String district,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "readingDate") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        return readingService.getReadingsByDistrict(district, page, size, sortBy, sortDir);
    }
}