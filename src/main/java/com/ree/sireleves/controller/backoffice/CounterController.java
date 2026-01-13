package com.ree.sireleves.controller.backoffice;

import com.ree.sireleves.dto.dashboard.CounterCreateRequest;
import com.ree.sireleves.dto.dashboard.CounterUpdateRequest;
import com.ree.sireleves.model.core.Counter;
import com.ree.sireleves.service.dashboard.CounterService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/backoffice/counters")
@PreAuthorize("hasRole('USER')")
public class CounterController {

    private final CounterService counterService;

    public CounterController(CounterService counterService) {
        this.counterService = counterService;
    }

    // CREATE
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Counter create(@Valid @RequestBody CounterCreateRequest request) {
        return counterService.create(request);
    }

    // READ - list
    @GetMapping
    public List<Counter> findAll() {
        return counterService.findAll();
    }

    // READ - detail
    @GetMapping("/{id}")
    public Counter findById(@PathVariable Long id) {
        return counterService.findById(id);
    }

    // UPDATE
    @PutMapping("/{id}")
    public Counter update(
            @PathVariable Long id,
            @Valid @RequestBody CounterUpdateRequest request
    ) {
        return counterService.update(id, request);
    }

    // DELETE (soft delete)
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        counterService.delete(id);
    }
}
