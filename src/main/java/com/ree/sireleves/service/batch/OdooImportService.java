package com.ree.sireleves.service.batch;

import com.ree.sireleves.model.core.Address;
import com.ree.sireleves.model.core.Client;
import com.ree.sireleves.repository.core.ClientRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@Transactional
public class OdooImportService {

    private final OdooXmlRpcClient odooClient;
    private final ClientRepository clientRepository;

    public OdooImportService(OdooXmlRpcClient odooClient,
                             ClientRepository clientRepository) {
        this.odooClient = odooClient;
        this.clientRepository = clientRepository;
    }

    @SuppressWarnings("unchecked")
    public int importClientsFromOdoo() throws Exception {

        Object[] partners = odooClient.fetchClients();
        Object[] odooAddresses = odooClient.fetchAddresses();

        int imported = 0;

        // ===== 1. IMPORT CLIENTS =====
        for (Object obj : partners) {
            Map<String, Object> p = (Map<String, Object>) obj;

            Long odooId = ((Number) p.get("id")).longValue();

            Client client = clientRepository
                    .findByOdooId(odooId)
                    .orElseGet(Client::new);

            client.setOdooId(odooId);

            String name = (String) p.get("name");
            if (name != null) {
                String[] parts = name.trim().split(" ", 2);
                client.setFirstName(parts[0]);
                client.setLastName(parts.length > 1 ? parts[1] : "");
            }

            client.setPhone(safeString(p.get("phone")));
            client.setEmail(safeString(p.get("email")));

            client.getAddresses().clear();

            clientRepository.save(client);
            imported++;
        }

        // ===== 2. IMPORT ADRESSES =====
        for (Object obj : odooAddresses) {
            Map<String, Object> addr = (Map<String, Object>) obj;

            Object[] parent = (Object[]) addr.get("parent_id");
            if (parent == null) continue;

            Long odooClientId = ((Number) parent[0]).longValue();

            Client client = clientRepository
                    .findByOdooId(odooClientId)
                    .orElse(null);

            if (client == null) continue;

            Address address = new Address();
            address.setStreet(buildStreet(
                    safeString(addr.get("street")),
                    safeString(addr.get("street2"))
            ));
            address.setCity(formatCity(safeString(addr.get("city"))));

            address.setClient(client);

            client.getAddresses().add(address);
        }

        return imported;
    }

    private String safeString(Object o) {
        if (o == null || o instanceof Boolean) return null;
        return o.toString();
    }


    private String buildStreet(String street1, String street2) {
        if (street1 == null) return street2;
        if (street2 == null) return street1;
        return street1 + ", " + street2;
    }

    private String formatCity(String city) {
        if (city == null || city.isBlank()) return city;
        return city.substring(0, 1).toUpperCase() + city.substring(1).toLowerCase();
    }

}
