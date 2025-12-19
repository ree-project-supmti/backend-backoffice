package com.ree.sireleves.service.mobile;

import com.ree.sireleves.model.Agent;
import com.ree.sireleves.model.core.Counter;
import com.ree.sireleves.repository.AgentRepository;
import com.ree.sireleves.repository.core.CounterRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MobileTourService {

    private final AgentRepository agentRepository;
    private final CounterRepository counterRepository;

    public MobileTourService(
            AgentRepository agentRepository,
            CounterRepository counterRepository
    ) {
        this.agentRepository = agentRepository;
        this.counterRepository = counterRepository;
    }

    public List<Counter> getTourneeForAgent(Long agentId) {
        Agent agent = agentRepository.findById(agentId)
                .orElseThrow(() -> new EntityNotFoundException("Agent not found"));

        return counterRepository.findActiveCountersByDistrict(agent.getDistrict());
    }
}
