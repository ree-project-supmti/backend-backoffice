package com.ree.sireleves.controller.backoffice;



import com.ree.sireleves.dto.dashboard.CounterCreateRequest;
import com.ree.sireleves.dto.dashboard.CounterCreateRequest;
import com.ree.sireleves.model.core.Counter;
import com.ree.sireleves.service.dashboard.CounterService;
import com.ree.sireleves.service.dashboard.CounterService;
import jakarta.validation.Valid;
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
}

