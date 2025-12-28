package com.ree.sireleves.service.dashboard;


import com.ree.sireleves.dto.dashboard.CounterCreateRequest;
import com.ree.sireleves.model.core.*;
import com.ree.sireleves.model.enums.CounterType;
import com.ree.sireleves.repository.core.AddressRepository;
import com.ree.sireleves.repository.core.CounterRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

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



    public Counter create(CounterCreateRequest request) {

        Address address = addressRepository.findById(request.addressId())
                .orElseThrow(() -> new IllegalArgumentException("Adresse introuvable"));



        long count = counterRepository.countByAddress_Id(address.getId());

        if (!addressIsBuilding(address) && count >= 2) {
            throw new IllegalStateException("Adresse déjà équipée (max 2 compteurs)");
        }

        if (counterRepository.existsByAddress_IdAndType(address.getId(), request.type())) {
            throw new IllegalStateException("Compteur de ce type déjà existant");
        }

        Client client = address.getClient();
        if (client == null) {
            throw new IllegalStateException("Address has no client");
        }
        // 🔢 Génération du serialNumber (9 chiffres)
        String maxSerial = counterRepository.findMaxSerialNumber();
        long next = (maxSerial == null) ? 1 : Long.parseLong(maxSerial) + 1;
        String serialNumber = String.format("%09d", next);

        Counter counter = new Counter();
        counter.setSerialNumber(serialNumber);
        counter.setType(request.type());
        counter.setAddress(address);
        counter.setActive(true);
        counter.setOdooId(null); // sera rempli lors du sync Odoo
        counter.setClient(client);

        return counterRepository.save(counter);
    }

    private boolean addressIsBuilding(Address address) {
        // à adapter plus tard (flag immeuble)
        return false;
    }
}


