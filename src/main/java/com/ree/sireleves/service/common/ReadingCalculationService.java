package com.ree.sireleves.service.common;

import org.springframework.stereotype.Service;

@Service
public class ReadingCalculationService {

    public int calculateConsumption(int previousIndex, int currentIndex) {
        if (currentIndex < previousIndex) {
            throw new IllegalArgumentException("Invalid meter index");
        }
        return currentIndex - previousIndex;
    }
}
