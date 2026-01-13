package com.ree.sireleves.service.backoffice;

import com.ree.sireleves.dto.backoffice.AgentDTO;
import com.ree.sireleves.model.Agent;
import com.ree.sireleves.repository.AgentRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class AgentService {

    private final AgentRepository agentRepository;

    public AgentService(AgentRepository agentRepository) {
        this.agentRepository = agentRepository;
    }

    public Page<AgentDTO> getAllActiveAgents(int page, int size, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("desc") 
            ? Sort.by(sortBy).descending() 
            : Sort.by(sortBy).ascending();
        
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Agent> agentPage = agentRepository.findByActiveTrue(pageable);
        
        return agentPage.map(this::convertToDTO);
    }

    public Page<AgentDTO> getAgentsByDistrict(String district, int page, int size, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("desc") 
            ? Sort.by(sortBy).descending() 
            : Sort.by(sortBy).ascending();
        
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Agent> agentPage = agentRepository.findByDistrictAndActiveTrue(district, pageable);
        
        return agentPage.map(this::convertToDTO);
    }

    private AgentDTO convertToDTO(Agent agent) {
        return new AgentDTO(
                agent.getId(),
                agent.getOdooId(),
                agent.getFirstName(),
                agent.getLastName(),
                agent.getPhone(),
                agent.getDistrict(),
                agent.getActive()
        );
    }
}