package com.ree.sireleves.service.dashboard;

import com.ree.sireleves.dto.dashboard.CounterCreateRequest;
import com.ree.sireleves.dto.dashboard.CounterUpdateRequest;
import com.ree.sireleves.model.core.Address;
import com.ree.sireleves.model.core.Client;
import com.ree.sireleves.model.core.Counter;
import com.ree.sireleves.repository.core.AddressRepository;
import com.ree.sireleves.repository.core.CounterRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class CounterService {

    private final CounterRepository counterRepository;
    private final AddressRepository addressRepository;

    public CounterService(CounterRepository counterRepository,
                          AddressRepository addressRepository) {
        this.counterRepository = counterRepository;
        this.addressRepository = addressRepository;
    }

    // ================= CREATE =================

    public Counter create(CounterCreateRequest request) {

        Address address = addressRepository.findById(request.addressId())
                .orElseThrow(() -> new IllegalArgumentException("Adresse introuvable"));

        Client client = address.getClient();
        if (client == null) {
            throw new IllegalStateException("Adresse sans client");
        }

        long count = counterRepository.countByAddress_Id(address.getId());

        if (!addressIsBuilding(address) && count >= 2) {
            throw new IllegalStateException("Adresse déjà équipée (max 2 compteurs)");
        }

        if (counterRepository.existsByAddress_IdAndType(address.getId(), request.type())) {
            throw new IllegalStateException("Compteur de ce type déjà existant");
        }

        // Génération serialNumber (9 chiffres)
        String maxSerial = counterRepository.findMaxSerialNumber();
        long next = (maxSerial == null) ? 1 : Long.parseLong(maxSerial) + 1;
        String serialNumber = String.format("%09d", next);

        Counter counter = new Counter();
        counter.setSerialNumber(serialNumber);
        counter.setType(request.type());
        counter.setAddress(address);
        counter.setClient(client);
        counter.setActive(true);
        counter.setOdooId(null);

        return counterRepository.save(counter);
    }

    // ================= READ =================

    public List<Counter> findAll() {
        return counterRepository.findAll();
    }

    public Counter findById(Long id) {
        return counterRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Compteur introuvable"));
    }

    // ================= UPDATE =================

    public Counter update(Long id, CounterUpdateRequest request) {
        Counter counter = findById(id);

        counter.setActive(request.active());

        return counterRepository.save(counter);
    }

    // ================= DELETE (soft) =================

    public void delete(Long id) {
        Counter counter = findById(id);
        counter.setActive(false);
        counterRepository.save(counter);
    }

    private boolean addressIsBuilding(Address address) {
        // future règle métier REE
        return false;
    }
}
