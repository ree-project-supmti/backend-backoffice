package com.ree.sireleves.service.mobile;

import com.ree.sireleves.dto.mobile.MobileSearchResultDTO;
import com.ree.sireleves.model.Agent;
import com.ree.sireleves.model.core.Counter;
import com.ree.sireleves.repository.AgentRepository;
import com.ree.sireleves.repository.ReadingRepository;
import com.ree.sireleves.repository.core.CounterRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MobileSearchService {

    private final CounterRepository counterRepository;
    private final AgentRepository agentRepository;
    private final ReadingRepository readingRepository;

    public MobileSearchService(
            CounterRepository counterRepository,
            AgentRepository agentRepository,
            ReadingRepository readingRepository
    ) {
        this.counterRepository = counterRepository;
        this.agentRepository = agentRepository;
        this.readingRepository = readingRepository;
    }

    public List<MobileSearchResultDTO> search(Long agentId, String query) {
        // Get agent's district
        Agent agent = agentRepository.findById(agentId)
                .orElseThrow(() -> new RuntimeException("Agent not found"));

        String district = agent.getDistrict();

        // If query is empty, return all counters in district
        String searchQuery = query == null ? "" : query.trim();

        // Search counters in district
        List<Counter> counters = counterRepository.searchInDistrict(district, searchQuery);

        // Map to DTOs with read status
        return counters.stream()
                .map(counter -> mapToSearchResult(counter))
                .collect(Collectors.toList());
    }

    private MobileSearchResultDTO mapToSearchResult(Counter counter) {
        // Determine match type based on query
        String matchType = "SERIAL"; // Default, could be enhanced to detect actual match type

        // Check if counter has been read this month
        boolean read = hasBeenReadThisMonth(counter.getId());

        // Build full address
        String fullAddress = counter.getAddress() != null 
                ? counter.getAddress().getFullAddress() 
                : "";

        return new MobileSearchResultDTO(
                counter.getId(),
                counter.getSerialNumber(),
                counter.getType(),
                fullAddress,
                matchType,
                read
        );
    }

    private boolean hasBeenReadThisMonth(Long counterId) {
        // Check if there's a validated reading for this counter this month
        // Note: The existsValidatedReading method checks for any validated reading
        // In a production system, this should be enhanced to check specifically for current month
        return readingRepository.existsValidatedReading(counterId);
    }
}
