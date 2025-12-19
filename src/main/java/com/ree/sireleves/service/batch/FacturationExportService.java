package com.ree.sireleves.service.batch;

import com.ree.sireleves.model.Reading;
import com.ree.sireleves.model.enums.ReadingStatus;
import com.ree.sireleves.repository.ReadingRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class FacturationExportService {

    private final ReadingRepository readingRepository;

    public FacturationExportService(ReadingRepository readingRepository) {
        this.readingRepository = readingRepository;
    }

    @Transactional
    public List<Reading> exportValidatedReadings() {

        List<Reading> readings =
                readingRepository.findByStatus(ReadingStatus.VALIDATED);

        for (Reading r : readings) {
            // 🔗 ici appel API Odoo Facturation (à implémenter)
            r.setStatus(ReadingStatus.SENT);
            r.setSentAt(Instant.now());
        }

        return readings;
    }
}
