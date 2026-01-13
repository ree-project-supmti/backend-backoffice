package com.ree.sireleves.controller.backoffice;

import com.ree.sireleves.dto.dashboard.CounterCreateRequest;
import com.ree.sireleves.model.core.Counter;
import com.ree.sireleves.service.dashboard.CounterService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/backoffice/counters")
@PreAuthorize("hasRole('USER')")
public class CounterController {

    private final CounterService counterService;

    public CounterController(CounterService counterService) {
        this.counterService = counterService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Counter create(@Valid @RequestBody CounterCreateRequest request) {
        return counterService.create(request);
    }

    @GetMapping
    public Page<Counter> getAllCounters(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "serialNumber") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        return counterService.getAllCounters(page, size, sortBy, sortDir);
    }

    @GetMapping("/district/{district}")
    public Page<Counter> getCountersByDistrict(
            @PathVariable String district,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "serialNumber") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        return counterService.getCountersByDistrict(district, page, size, sortBy, sortDir);
    }
}

