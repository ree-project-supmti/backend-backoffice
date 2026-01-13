package com.ree.sireleves.service.batch;


import com.ree.sireleves.model.Reading;
import com.ree.sireleves.model.core.Address;
import com.ree.sireleves.model.core.Client;
import com.ree.sireleves.model.core.Counter;
import com.ree.sireleves.model.enums.CounterType;
import com.ree.sireleves.model.enums.ReadingStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ree.sireleves.repository.ReadingRepository;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class OdooExportService {

    private final OdooXmlRpcClient odooClient;
    private final ReadingRepository readingRepository;
    private final ConsumptionService consumptionService;

    public OdooExportService(
            OdooXmlRpcClient odooClient,
            ReadingRepository readingRepository,
            ConsumptionService consumptionService
    ) {
        this.odooClient = odooClient;
        this.readingRepository = readingRepository;
        this.consumptionService = consumptionService;
    }

    public int exportValidatedReadingsToOdoo() throws Exception {

        List<Reading> readings =
                readingRepository.findByStatusAndSentAtIsNull(ReadingStatus.VALIDATED);

        int exported = 0;

        for (Reading reading : readings) {

            Counter counter = reading.getCounter();
            Client client = counter.getClient();

            // --- sécurité ---
            if (client.getOdooId() == null) continue;

            // --- lecture précédente ---
            Integer previousValue = readingRepository
                    .findFirstByCounterAndReadingDateBeforeOrderByReadingDateDesc(
                            counter,
                            reading.getReadingDate()
                    )
                    .map(Reading::getValue)
                    .orElse(null);

            double consumption =
                    consumptionService.calculate(reading.getValue(), previousValue);

            Double water = null;
            Double electricity = null;

            if (counter.getType() == CounterType.WATER) {
                water = consumption;
                electricity = 0d;
            } else {
                electricity = consumption;
                water = 0d;
            }

            Map<String, Object> payload = new HashMap<>();
            payload.put("x_client_odoo_id", client.getOdooId());
            payload.put("x_counter_number", counter.getSerialNumber());
            payload.put("x_reading_datetime", reading.getReadingDate().toString());
            payload.put("x_water_volume", water);
            payload.put("x_electricity_volume", electricity);
            payload.put("x_origin_reading_id", reading.getMobileUuid());
            payload.put("x_sent_at", Instant.now().toString());

            Address address = counter.getAddress();
            if (address != null) {
                payload.put(
                        "x_address",
                        buildFullAddress(address)
                );
            }

            // --- ENVOI ODOO ---
            odooClient.createMeterReading(payload);

            // --- marquer comme envoyé ---
            reading.setSentAt(Instant.now());
            reading.setStatus(ReadingStatus.SENT);

            exported++;
        }

        return exported;
    }

    private String buildFullAddress(Address a) {
        return String.join(", ",
                safe(a.getStreet()),
                safe(a.getCity())
        );
    }

    private String safe(String s) {
        return s == null ? "" : s;
    }
}
