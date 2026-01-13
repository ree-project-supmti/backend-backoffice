package com.ree.sireleves.service.mobile;

import com.ree.sireleves.dto.mobile.MobileBatchReadingResponseDTO;
import com.ree.sireleves.dto.mobile.MobileReadingRequestDTO;
import com.ree.sireleves.dto.mobile.ReadingErrorDTO;
import com.ree.sireleves.exception.CounterNotFoundException;
import com.ree.sireleves.exception.InvalidReadingException;
import com.ree.sireleves.exception.UnauthorizedDistrictAccessException;
import com.ree.sireleves.model.Agent;
import com.ree.sireleves.model.Reading;
import com.ree.sireleves.model.core.Counter;
import com.ree.sireleves.model.enums.ReadingStatus;
import com.ree.sireleves.repository.AgentRepository;
import com.ree.sireleves.repository.ReadingRepository;
import com.ree.sireleves.repository.core.CounterRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class MobileReadingService {

    private final ReadingRepository readingRepository;
    private final CounterRepository counterRepository;
    private final AgentRepository agentRepository;

    public MobileReadingService(
            ReadingRepository readingRepository,
            CounterRepository counterRepository,
            AgentRepository agentRepository
    ) {
        this.readingRepository = readingRepository;
        this.counterRepository = counterRepository;
        this.agentRepository = agentRepository;
    }

    @Transactional
    public Reading submitReading(Reading reading) {

        readingRepository.findByMobileUuid(reading.getMobileUuid())
                .ifPresent(existing -> {
                    throw InvalidReadingException.duplicateReading(reading.getMobileUuid());
                });

        reading.setStatus(ReadingStatus.PENDING);
        reading.setCreatedAt(Instant.now());

        return readingRepository.save(reading);
    }

    @Transactional
    public MobileBatchReadingResponseDTO submitBatchReadings(
            List<MobileReadingRequestDTO> readingRequests,
            Long authenticatedAgentId
    ) {
        List<ReadingErrorDTO> errors = new ArrayList<>();
        int successCount = 0;

        // Get the authenticated agent
        Agent agent = agentRepository.findById(authenticatedAgentId)
                .orElseThrow(() -> new IllegalArgumentException("Agent not found"));

        for (MobileReadingRequestDTO request : readingRequests) {
            try {
                // Validate each reading independently
                validateReading(request, agent);
                
                // Create and save the reading
                Reading reading = createReadingFromRequest(request, agent);
                readingRepository.save(reading);
                successCount++;
                
            } catch (Exception e) {
                // Collect errors for invalid readings
                errors.add(new ReadingErrorDTO(
                        request.mobileUuid(),
                        request.counterId(),
                        e.getMessage()
                ));
            }
        }

        int totalSubmitted = readingRequests.size();
        int failureCount = errors.size();

        return new MobileBatchReadingResponseDTO(
                totalSubmitted,
                successCount,
                failureCount,
                errors
        );
    }

    private void validateReading(MobileReadingRequestDTO request, Agent agent) {
        // Check for duplicate mobile UUID
        Optional<Reading> existingReading = readingRepository.findByMobileUuid(request.mobileUuid());
        if (existingReading.isPresent()) {
            throw InvalidReadingException.duplicateReading(request.mobileUuid());
        }

        // Validate counter exists and is active
        Counter counter = counterRepository.findById(request.counterId())
                .orElseThrow(() -> new CounterNotFoundException(request.counterId()));

        if (!Boolean.TRUE.equals(counter.getActive())) {
            throw InvalidReadingException.inactiveCounter(request.counterId());
        }

        // Validate counter is in agent's district
        if (counter.getAddress() == null || !agent.getDistrict().equals(counter.getAddress().getDistrict())) {
            throw new UnauthorizedDistrictAccessException(
                    agent.getDistrict(),
                    counter.getAddress() != null ? counter.getAddress().getDistrict() : "unknown"
            );
        }

        // Validate new index >= old index
        Optional<Reading> lastReading = readingRepository.findLastReadingByCounter(request.counterId());
        if (lastReading.isPresent()) {
            Integer previousIndex = lastReading.get().getValue();
            if (request.value() < previousIndex) {
                throw InvalidReadingException.invalidIndex(request.value(), previousIndex);
            }
        }
    }

    private Reading createReadingFromRequest(MobileReadingRequestDTO request, Agent agent) {
        Reading reading = new Reading();
        
        // Set counter
        Counter counter = counterRepository.findById(request.counterId())
                .orElseThrow(() -> new CounterNotFoundException(request.counterId()));
        reading.setCounter(counter);
        
        // Set agent
        reading.setAgent(agent);
        
        // Set reading data
        reading.setValue(request.value());
        reading.setReadingDate(request.readingDate());
        reading.setLatitude(request.latitude());
        reading.setLongitude(request.longitude());
        reading.setMobileUuid(request.mobileUuid());
        
        // Set status and timestamp
        reading.setStatus(ReadingStatus.PENDING);
        reading.setCreatedAt(Instant.now());
        
        return reading;
    }
}
