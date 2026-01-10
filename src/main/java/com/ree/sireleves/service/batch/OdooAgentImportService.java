package com.ree.sireleves.service.batch;

import com.ree.sireleves.model.Agent;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ree.sireleves.repository.AgentRepository;


import java.util.Map;
import java.util.Random;

@Service
@Transactional
public class OdooAgentImportService {

    private final OdooXmlRpcClient odooClient;
    private final AgentRepository agentRepository;

    public OdooAgentImportService(OdooXmlRpcClient odooClient, AgentRepository agentRepository) {
        this.odooClient = odooClient;
        this.agentRepository = agentRepository;
    }

    @SuppressWarnings("unchecked")
    public int importAgentsFromOdoo() throws Exception {

        Object[] employees = odooClient.fetchAgents();
        int imported = 0;

        for (Object obj : employees) {
            Map<String, Object> e = (Map<String, Object>) obj;

            Long odooId = ((Number) e.get("id")).longValue();

            Agent agent = agentRepository
                    .findByOdooId(odooId)
                    .orElseGet(Agent::new);

            agent.setOdooId(odooId);

            // ----- Name -----
            String name = (String) e.get("name");
            if (name != null) {
                String[] parts = name.trim().split(" ", 2);
                agent.setFirstName(parts[0]);
                agent.setLastName(parts.length > 1 ? parts[1] : "");
            }

            // ----- Phone -----
            String phone = safeString(e.get("work_phone"));
            if (phone == null) {
                phone = safeString(e.get("mobile_phone"));
            }
            agent.setPhone(phone);

            // ----- District -----
            Object[] department = (Object[]) e.get("department_id");
            if (department != null) {
                agent.setDistrict(formatDistrict((String) department[1]));
            }

            // ----- Secret Code -----
            if (agent.getSecretCode() == null) {
                agent.setSecretCode(generateSecretCode());
            }

            agent.setActive(true);

            agentRepository.save(agent);
            imported++;
        }

        return imported;
    }

    // ================= HELPERS =================

    private String safeString(Object o) {
        if (o == null || o instanceof Boolean) return null;
        return o.toString();
    }

    private String formatDistrict(String district) {
        if (district == null) return null;
        return district.substring(0, 1).toUpperCase()
                + district.substring(1).toLowerCase();
    }

    private String generateSecretCode() {
        int code = 100000 + new Random().nextInt(900000);
        return String.valueOf(code);
    }
}
