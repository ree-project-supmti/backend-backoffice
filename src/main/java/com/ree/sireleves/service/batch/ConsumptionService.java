package com.ree.sireleves.service.batch;

import org.springframework.stereotype.Service;

@Service
public class ConsumptionService {

    public double calculate(Integer current, Integer previous) {
        if (previous == null) return 0d;
        return Math.max(0, current - previous);
    }
}
