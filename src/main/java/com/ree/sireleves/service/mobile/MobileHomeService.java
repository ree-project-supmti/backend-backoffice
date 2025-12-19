package com.ree.sireleves.service.mobile;

import com.ree.sireleves.dto.mobile.MobileHomeAddressDTO;
import com.ree.sireleves.dto.mobile.MobileHomeCounterDTO;
import com.ree.sireleves.model.core.Address;
import com.ree.sireleves.model.core.Counter;
import com.ree.sireleves.model.enums.CounterType;
import com.ree.sireleves.repository.core.AddressRepository;
import com.ree.sireleves.repository.core.CounterRepository;
import com.ree.sireleves.repository.ReadingRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MobileHomeService {

    private final AddressRepository addressRepository;
    private final CounterRepository counterRepository;
    private final ReadingRepository readingRepository;

    public MobileHomeService(
            AddressRepository addressRepository,
            CounterRepository counterRepository,
            ReadingRepository readingRepository
    ) {
        this.addressRepository = addressRepository;
        this.counterRepository = counterRepository;
        this.readingRepository = readingRepository;
    }

    /* =========================
       Vue ADRESSES
       ========================= */
    public List<MobileHomeAddressDTO> getAddresses(
            String district,
            Boolean read
    ) {
        return addressRepository.findByDistrict(district)
                .stream()
                .map(address -> {
                    int total = counterRepository.countByAddress(address);
                    int readCount = readingRepository.countReadByAddress(address.getId());

                    boolean fullyRead = total > 0 && total == readCount;

                    if (read != null && read != fullyRead) {
                        return null;
                    }

                    return new MobileHomeAddressDTO(
                            address.getId(),
                            address.getFullAddress(),
                            address.getDistrict(),
                            total,
                            readCount,
                            fullyRead
                    );
                })
                .filter(dto -> dto != null)
                .collect(Collectors.toList());
    }

    /* =========================
       Vue COMPTEURS
       ========================= */
    public List<MobileHomeCounterDTO> getCounters(
            String district,
            CounterType type,
            Boolean read
    ) {
        return counterRepository.findByAddress_District(district)
                .stream()
                .map(counter -> {
                    boolean isRead =
                            readingRepository.existsValidatedReading(counter.getId());

                    if (type != null && counter.getType() != type) return null;
                    if (read != null && read != isRead) return null;

                    return new MobileHomeCounterDTO(
                            counter.getId(),
                            counter.getSerialNumber(),
                            counter.getType(),
                            counter.getAddress().getFullAddress(),
                            isRead
                    );
                })
                .filter(dto -> dto != null)
                .collect(Collectors.toList());
    }
}
