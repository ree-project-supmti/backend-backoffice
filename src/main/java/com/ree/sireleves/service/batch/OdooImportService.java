package com.ree.sireleves.service.batch;

import com.ree.sireleves.model.Agent;
import com.ree.sireleves.model.core.Client;
import com.ree.sireleves.model.core.Counter;
import com.ree.sireleves.repository.AgentRepository;
import com.ree.sireleves.repository.core.ClientRepository;
import com.ree.sireleves.repository.core.CounterRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OdooImportService {

    private final ClientRepository clientRepository;
    private final AgentRepository agentRepository;
    private final CounterRepository counterRepository;

    public OdooImportService(
            ClientRepository clientRepository,
            AgentRepository agentRepository,
            CounterRepository counterRepository
    ) {
        this.clientRepository = clientRepository;
        this.agentRepository = agentRepository;
        this.counterRepository = counterRepository;
    }

    @Transactional
    public void importClients(List<Client> clients) {
        for (Client client : clients) {
            clientRepository.findByOdooId(client.getOdooId())
                    .ifPresentOrElse(
                            existing -> {
                                existing.setName(client.getName());
                                existing.setPhone(client.getPhone());
                                existing.setEmail(client.getEmail());
                            },
                            () -> clientRepository.save(client)
                    );
        }
    }

    @Transactional
    public void importAgents(List<Agent> agents) {
        for (Agent agent : agents) {
            agentRepository.findByOdooId(agent.getOdooId())
                    .ifPresentOrElse(
                            existing -> {
                                existing.setDistrict(agent.getDistrict());
                                existing.setActive(agent.getActive());
                            },
                            () -> agentRepository.save(agent)
                    );
        }
    }

    @Transactional
    public void importCounters(List<Counter> counters) {
        for (Counter counter : counters) {
            counterRepository.findByOdooId(counter.getOdooId())
                    .ifPresentOrElse(
                            existing -> {
                                existing.setActive(counter.getActive());
                            },
                            () -> counterRepository.save(counter)
                    );
        }
    }
}
