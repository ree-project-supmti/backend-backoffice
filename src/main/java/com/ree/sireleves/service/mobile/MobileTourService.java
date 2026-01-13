package com.ree.sireleves.service.mobile;

import com.ree.sireleves.dto.mobile.MobileTourAddressDTO;
import com.ree.sireleves.dto.mobile.MobileTourCounterDetailDTO;
import com.ree.sireleves.dto.mobile.MobileTourDownloadDTO;
import com.ree.sireleves.model.Agent;
import com.ree.sireleves.model.Reading;
import com.ree.sireleves.model.core.Address;
import com.ree.sireleves.model.core.Counter;
import com.ree.sireleves.repository.AgentRepository;
import com.ree.sireleves.repository.ReadingRepository;
import com.ree.sireleves.repository.core.CounterRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class MobileTourService {

    private final AgentRepository agentRepository;
    private final CounterRepository counterRepository;
    private final ReadingRepository readingRepository;

    public MobileTourService(
            AgentRepository agentRepository,
            CounterRepository counterRepository,
            ReadingRepository readingRepository
    ) {
        this.agentRepository = agentRepository;
        this.counterRepository = counterRepository;
        this.readingRepository = readingRepository;
    }

    public List<Counter> getTourneeForAgent(Long agentId) {
        Agent agent = agentRepository.findById(agentId)
                .orElseThrow(() -> new IllegalArgumentException("Agent not found"));

        return counterRepository.findActiveCountersByDistrict(agent.getDistrict());
    }

    public MobileTourDownloadDTO downloadTour(Long agentId) {
        // Retrieve agent
        Agent agent = agentRepository.findById(agentId)
                .orElseThrow(() -> new IllegalArgumentException("Agent not found"));

        // Retrieve all active counters in agent's district
        List<Counter> allCounters = counterRepository.findActiveCountersByDistrict(agent.getDistrict());

        // Get current month boundaries
        YearMonth currentMonth = YearMonth.now();
        ZonedDateTime startOfMonth = currentMonth.atDay(1).atStartOfDay(ZoneId.systemDefault());
        ZonedDateTime endOfMonth = currentMonth.atEndOfMonth().atTime(23, 59, 59).atZone(ZoneId.systemDefault());
        Instant monthStart = startOfMonth.toInstant();
        Instant monthEnd = endOfMonth.toInstant();

        // Get counter IDs that have been read this month in the district
        List<Long> readCounterIds = readingRepository.findCounterIdsReadInPeriod(
                agent.getDistrict(),
                monthStart,
                monthEnd
        );
        Set<Long> readCounterIdSet = new HashSet<>(readCounterIds);

        // Filter out counters already read this month
        List<Counter> unreadCounters = allCounters.stream()
                .filter(counter -> !readCounterIdSet.contains(counter.getId()))
                .collect(Collectors.toList());

        // Get last reading for each counter
        Map<Long, Integer> lastReadingValues = new HashMap<>();
        for (Counter counter : unreadCounters) {
            Optional<Reading> lastReading = readingRepository.findLastReadingByCounter(counter.getId());
            lastReading.ifPresent(reading -> lastReadingValues.put(counter.getId(), reading.getValue()));
        }

        // Group counters by address
        Map<Address, List<Counter>> countersByAddress = unreadCounters.stream()
                .filter(counter -> counter.getAddress() != null)
                .collect(Collectors.groupingBy(Counter::getAddress));

        // Build address DTOs
        List<MobileTourAddressDTO> addressDTOs = countersByAddress.entrySet().stream()
                .map(entry -> {
                    Address address = entry.getKey();
                    List<Counter> counters = entry.getValue();

                    List<MobileTourCounterDetailDTO> counterDTOs = counters.stream()
                            .map(counter -> new MobileTourCounterDetailDTO(
                                    counter.getId(),
                                    counter.getSerialNumber(),
                                    counter.getType(),
                                    lastReadingValues.get(counter.getId()),
                                    counter.getClient() != null ? counter.getClient().getName() : null
                            ))
                            .collect(Collectors.toList());

                    return new MobileTourAddressDTO(
                            address.getId(),
                            address.getFullAddress(),
                            counterDTOs
                    );
                })
                .collect(Collectors.toList());

        return new MobileTourDownloadDTO(
                agent.getDistrict(),
                allCounters.size(),
                unreadCounters.size(),
                addressDTOs
        );
    }
}
