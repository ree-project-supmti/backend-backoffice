package com.ree.sireleves.service.dashboard;

import com.ree.sireleves.repository.core.CounterRepository;
import com.ree.sireleves.repository.ReadingRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class DashboardService {

    private final CounterRepository counterRepository;
    private final ReadingRepository readingRepository;

    public DashboardService(
            CounterRepository counterRepository,
            ReadingRepository readingRepository
    ) {
        this.counterRepository = counterRepository;
        this.readingRepository = readingRepository;
    }

    public double coverageRateByDistrict(String district, Instant start, Instant end) {
        long totalCounters = counterRepository.countActiveByDistrict(district);
        if (totalCounters == 0) return 0.0;

        long readingsDone =
                readingRepository.countByDistrictAndPeriod(district, start, end);

        return (double) readingsDone / totalCounters * 100.0;
    }

    public long readingsByAgent(Long agentId, Instant start, Instant end) {
        return readingRepository.countByAgentAndPeriod(agentId, start, end);
    }
}
