package com.ree.sireleves.service.mobile;

import com.ree.sireleves.dto.mobile.MobileAgentProfileDTO;
import com.ree.sireleves.model.Agent;
import com.ree.sireleves.repository.AgentRepository;
import com.ree.sireleves.repository.ReadingRepository;
import com.ree.sireleves.repository.core.CounterRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@Service
public class MobileAgentService {

    private final AgentRepository agentRepository;
    private final CounterRepository counterRepository;
    private final ReadingRepository readingRepository;

    public MobileAgentService(
            AgentRepository agentRepository,
            CounterRepository counterRepository,
            ReadingRepository readingRepository
    ) {
        this.agentRepository = agentRepository;
        this.counterRepository = counterRepository;
        this.readingRepository = readingRepository;
    }

    /**
     * Retrieves the profile information for an agent.
     * 
     * @param agentId The ID of the agent
     * @return MobileAgentProfileDTO containing agent details and statistics
     * @throws RuntimeException if agent not found
     */
    public MobileAgentProfileDTO getAgentProfile(Long agentId) {
        // Retrieve agent details
        Agent agent = agentRepository.findById(agentId)
                .orElseThrow(() -> new RuntimeException("Agent not found"));

        // Count total counters in district
        long totalCounters = counterRepository.countActiveByDistrict(agent.getDistrict());

        // Count readings completed this month
        Instant[] monthBounds = getCurrentMonthBounds();
        long readingsThisMonth = readingRepository.countByAgentAndPeriod(
                agentId,
                monthBounds[0],
                monthBounds[1]
        );

        // Map to DTO
        return new MobileAgentProfileDTO(
                agent.getId(),
                agent.getFirstName(),
                agent.getLastName(),
                agent.getPhone(),
                agent.getDistrict(),
                (int) totalCounters,
                (int) readingsThisMonth
        );
    }

    /**
     * Calculates the start and end instants for the current month.
     * 
     * @return Array with [startOfMonth, endOfMonth]
     */
    private Instant[] getCurrentMonthBounds() {
        ZonedDateTime now = ZonedDateTime.now(ZoneId.systemDefault());
        ZonedDateTime startOfMonth = now.withDayOfMonth(1)
                .withHour(0)
                .withMinute(0)
                .withSecond(0)
                .withNano(0);
        ZonedDateTime endOfMonth = startOfMonth.plusMonths(1).minusNanos(1);

        return new Instant[]{startOfMonth.toInstant(), endOfMonth.toInstant()};
    }
}
