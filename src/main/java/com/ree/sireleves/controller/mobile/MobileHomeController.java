package com.ree.sireleves.controller.mobile;

import com.ree.sireleves.dto.mobile.MobileHomeAddressDTO;
import com.ree.sireleves.dto.mobile.MobileHomeCounterDTO;
import com.ree.sireleves.model.enums.CounterType;
import com.ree.sireleves.service.mobile.MobileHomeService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/mobile/home")
@PreAuthorize("hasRole('AGENT')")
public class MobileHomeController {

    private final MobileHomeService homeService;

    public MobileHomeController(MobileHomeService homeService) {
        this.homeService = homeService;
    }

    /* =========================
       Vue ADRESSES
       ========================= */
    @GetMapping("/addresses")
    public List<MobileHomeAddressDTO> addresses(
            @RequestParam String district,
            @RequestParam(required = false) Boolean read
    ) {
        return homeService.getAddresses(district, read);
    }

    /* =========================
       Vue COMPTEURS
       ========================= */
    @GetMapping("/counters")
    public List<MobileHomeCounterDTO> counters(
            @RequestParam String district,
            @RequestParam(required = false) CounterType type,
            @RequestParam(required = false) Boolean read
    ) {
        return homeService.getCounters(district, type, read);
    }
}
