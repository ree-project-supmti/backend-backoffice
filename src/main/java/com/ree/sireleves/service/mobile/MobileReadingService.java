package com.ree.sireleves.service.mobile;

import com.ree.sireleves.model.Reading;
import com.ree.sireleves.model.enums.ReadingStatus;
import com.ree.sireleves.repository.ReadingRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class MobileReadingService {

    private final ReadingRepository readingRepository;

    public MobileReadingService(ReadingRepository readingRepository) {
        this.readingRepository = readingRepository;
    }

    @Transactional
    public Reading submitReading(Reading reading) {

        readingRepository.findByMobileUuid(reading.getMobileUuid())
                .ifPresent(existing -> {
                    throw new IllegalStateException("Duplicate mobileUuid");
                });

        reading.setStatus(ReadingStatus.PENDING);
        reading.setCreatedAt(Instant.now());

        return readingRepository.save(reading);
    }
}
