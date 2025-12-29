package com.ree.sireleves.service.mobile;

import com.ree.sireleves.dto.mobile.MobileReadingHistoryDTO;
import com.ree.sireleves.model.Reading;
import com.ree.sireleves.model.core.Counter;
import com.ree.sireleves.repository.AgentRepository;
import com.ree.sireleves.repository.ReadingRepository;
import com.ree.sireleves.repository.core.CounterRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Service
public class MobileCounterService {

    private final CounterRepository counterRepository;
    private final ReadingRepository readingRepository;
    private final AgentRepository agentRepository;

    public MobileCounterService(
            CounterRepository counterRepository,
            ReadingRepository readingRepository,
            AgentRepository agentRepository
    ) {
        this.counterRepository = counterRepository;
        this.readingRepository = readingRepository;
        this.agentRepository = agentRepository;
    }

    /**
     * Retrieves reading history for a counter.
     * Validates counter exists and agent has access to the counter's district.
     * Returns last 12 readings ordered by date descending with consumption calculated.
     */
    public List<MobileReadingHistoryDTO> getReadingHistory(Long counterId, Long agentId) {
        // Validate counter exists
        Counter counter = counterRepository.findById(counterId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Counter not found"
                ));

        // Validate agent has access to counter's district
        var agent = agentRepository.findById(agentId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.UNAUTHORIZED,
                        "Agent not found"
                ));

        if (!counter.getAddress().getDistrict().equals(agent.getDistrict())) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Agent does not have access to this counter's district"
            );
        }

        // Retrieve last 12 readings ordered by date descending
        List<Reading> readings = readingRepository.findLast12Readings(
                counterId,
                PageRequest.of(0, 12)
        );

        // Calculate consumption for each reading and map to DTO
        List<MobileReadingHistoryDTO> history = new ArrayList<>();
        for (int i = 0; i < readings.size(); i++) {
            Reading current = readings.get(i);
            Integer consumption = null;

            // Calculate consumption if there's a previous reading
            if (i < readings.size() - 1) {
                Reading previous = readings.get(i + 1);
                consumption = current.getValue() - previous.getValue();
            }

            String agentName = current.getAgent() != null
                    ? current.getAgent().getFirstName() + " " + current.getAgent().getLastName()
                    : "Unknown";

            history.add(new MobileReadingHistoryDTO(
                    current.getId(),
                    current.getValue(),
                    current.getReadingDate(),
                    consumption,
                    agentName
            ));
        }

        return history;
    }
}
