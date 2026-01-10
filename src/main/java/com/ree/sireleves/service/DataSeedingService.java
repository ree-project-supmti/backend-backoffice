package com.ree.sireleves.service;

import com.ree.sireleves.model.Agent;
import com.ree.sireleves.model.core.Address;
import com.ree.sireleves.model.core.Client;
import com.ree.sireleves.model.core.Counter;
import com.ree.sireleves.model.Reading;
import com.ree.sireleves.model.enums.CounterType;
import com.ree.sireleves.model.enums.ReadingStatus;
import com.ree.sireleves.repository.AgentRepository;
import com.ree.sireleves.repository.ReadingRepository;
import com.ree.sireleves.repository.core.AddressRepository;
import com.ree.sireleves.repository.core.ClientRepository;
import com.ree.sireleves.repository.core.CounterRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class DataSeedingService {

    private final AgentRepository agentRepository;
    private final ClientRepository clientRepository;
    private final AddressRepository addressRepository;
    private final CounterRepository counterRepository;
    private final ReadingRepository readingRepository;
    private final Random random = new Random();

    public DataSeedingService(
            AgentRepository agentRepository,
            ClientRepository clientRepository,
            AddressRepository addressRepository,
            CounterRepository counterRepository,
            ReadingRepository readingRepository
    ) {
        this.agentRepository = agentRepository;
        this.clientRepository = clientRepository;
        this.addressRepository = addressRepository;
        this.counterRepository = counterRepository;
        this.readingRepository = readingRepository;
    }

    @Transactional
    public void seedTestData() {
        System.out.println("🌱 Starting optimized data seeding...");

        // Clear existing data
        clearTestData();

        // Create smaller, optimized dataset for faster seeding
        List<Client> clients = createClients();
        List<Address> addresses = createAddresses(clients);
        List<Counter> counters = createCounters(addresses);
        createReadings(counters);

        System.out.println("✅  Data seeding completed!");
        System.out.println("📊 Created:");
        System.out.println("   - " + clients.size() + " clients");
        System.out.println("   - " + addresses.size() + " addresses");
        System.out.println("   - " + counters.size() + " counters");
        System.out.println("   - Sample readings for testing");
    }

    private void clearTestData() {
        readingRepository.deleteAll();
        counterRepository.deleteAll();
        addressRepository.deleteAll();
        clientRepository.deleteAll();
        System.out.println("🗑️ Cleared existing test data from MySQL");
    }
    
    public void clearAllData() {
        readingRepository.deleteAll();
        counterRepository.deleteAll();
        addressRepository.deleteAll();
        clientRepository.deleteAll();
        System.out.println("🗑️ Cleared all data from MySQL database");
    }

    private List<Client> createClients() {
        List<Client> clients = new ArrayList<>();
        
        String[] firstNames = {
            "Mohammed", "Fatima", "Ahmed", "Khadija", "Hassan", "Aicha", "Youssef", "Zineb", 
            "Omar", "Salma", "Karim", "Nadia", "Rachid", "Samira", "Mehdi", "Laila"
        };
        
        String[] lastNames = {
            "Alami", "Benali", "Idrissi", "Fassi", "Tazi", "Benjelloun", "Chraibi", "Kettani", 
            "Lahlou", "Mekouar", "Naciri", "Ouazzani", "Sefrioui", "Tounsi", "Zniber", "Berrada"
        };

        for (int i = 1; i <= 30; i++) {
            Client client = new Client();
            client.setOdooId((long) i);
            client.setName(firstNames[i % firstNames.length] + " " + lastNames[i % lastNames.length]);
            client.setPhone("+212 " + (random.nextBoolean() ? "6" : "5") + 
                           String.format("%02d", random.nextInt(100)) + 
                           String.format("%02d", random.nextInt(100)) + 
                           String.format("%02d", random.nextInt(100)));
            client.setCin(String.format("A%06d", random.nextInt(999999)));
            
            clients.add(client);
        }
        
        // Batch save all clients at once
        return clientRepository.saveAll(clients);
    }

    private List<Address> createAddresses(List<Client> clients) {
        List<Address> addresses = new ArrayList<>();
        
        String[] buildings = {
            "Résidence Al Amal", "Immeuble Yasmine", "Résidence Les Jardins", "Immeuble Riad",
            "Résidence Palmier", "Immeuble Souissi", "Résidence Océan", "Immeuble Hassan"
        };
        
        String[] streets = {
            "Avenue Mohammed V", "Rue Patrice Lumumba", "Avenue Allal Ben Abdellah", "Rue Oued Fès",
            "Avenue Hassan II", "Rue Abou Faris Al Marini", "Avenue Al Marsa", "Rue Tanger"
        };

        String[] districts = {"Agdal", "Hay Riad", "Souissi", "Hassan"};

        for (int i = 0; i < clients.size(); i++) {
            Client client = clients.get(i);
            String building = buildings[i % buildings.length];
            String street = streets[i % streets.length];
            String district = districts[i % districts.length];
            
            Address address = new Address();
            address.setOdooId((long) (i + 1));
            address.setClient(client);
            address.setBuildingName(building);
            address.setApartmentNumber(String.valueOf((i % 10) + 1) + String.format("%02d", (i / 10) + 1));
            address.setStreet(street);
            address.setDistrict(district);
            address.setPostalCode("10" + String.format("%03d", 100 + i));
            address.setCity("Rabat");
            address.setFullAddress(building + ", Apt " + address.getApartmentNumber() + 
                                 ", " + street + ", " + district + ", Rabat");
            
            // Add some coordinates for Rabat area
            address.setLatitude(33.9716 + (random.nextDouble() - 0.5) * 0.1);
            address.setLongitude(-6.8498 + (random.nextDouble() - 0.5) * 0.1);
            
            addresses.add(address);
        }
        
        // Batch save all addresses at once
        return addressRepository.saveAll(addresses);
    }

    private List<Counter> createCounters(List<Address> addresses) {
        List<Counter> counters = new ArrayList<>();
        long serialCounter = 1;
        
        for (Address address : addresses) {
            // Create water counter for each address
            Counter waterCounter = new Counter();
            waterCounter.setOdooId(serialCounter);
            waterCounter.setSerialNumber(String.format("%09d", serialCounter++));
            waterCounter.setAddress(address);
            waterCounter.setClient(address.getClient());
            waterCounter.setType(CounterType.WATER);
            waterCounter.setCurrentIndex(1000.0 + random.nextDouble() * 8000);
            waterCounter.setLastReadingDate(LocalDateTime.now().minusDays(random.nextInt(90)));
            waterCounter.setActive(true);
            counters.add(waterCounter);
            
            // Create electricity counter for 70% of addresses
            if (random.nextDouble() < 0.7) {
                Counter elecCounter = new Counter();
                elecCounter.setOdooId(serialCounter);
                elecCounter.setSerialNumber(String.format("%09d", serialCounter++));
                elecCounter.setAddress(address);
                elecCounter.setClient(address.getClient());
                elecCounter.setType(CounterType.ELECTRICITY);
                elecCounter.setCurrentIndex(5000.0 + random.nextDouble() * 25000);
                elecCounter.setLastReadingDate(LocalDateTime.now().minusDays(random.nextInt(90)));
                elecCounter.setActive(true);
                counters.add(elecCounter);
            }
        }
        
        // Batch save all counters at once
        return counterRepository.saveAll(counters);
    }

    private void createReadings(List<Counter> counters) {
        List<Reading> readings = new ArrayList<>();
        
        // Create readings for 50% of counters with minimal historical data
        int readingsToCreate = Math.min(counters.size() / 2, 20); // Limit to max 20 counters
        
        for (int i = 0; i < readingsToCreate; i++) {
            Counter counter = counters.get(i);
            Agent testAgent = agentRepository.findByOdooId(1L).orElse(null);
            
            if (testAgent != null) {
                // Create only 1-2 readings per counter for faster seeding
                int numReadings = 1 + random.nextInt(2);
                
                for (int j = 0; j < numReadings; j++) {
                    Reading reading = new Reading();
                    reading.setCounter(counter);
                    reading.setAgent(testAgent);
                    
                    // Calculate progressive reading values
                    double baseValue = counter.getCurrentIndex();
                    double increment = counter.getType() == CounterType.WATER ? 
                                     random.nextDouble() * 30 + 10 : 
                                     random.nextDouble() * 300 + 100;
                    
                    reading.setValue((int) (baseValue + (increment * (j + 1))));
                    reading.setReadingDate(LocalDateTime.now()
                                         .minusDays(30 - (j * 15) + random.nextInt(5))
                                         .atZone(ZoneId.systemDefault()).toInstant());
                    
                    // Simple status distribution
                    ReadingStatus[] statuses = {ReadingStatus.PENDING, ReadingStatus.VALIDATED};
                    reading.setStatus(statuses[random.nextInt(statuses.length)]);
                    
                    reading.setMobileUuid("MOBILE_" + System.currentTimeMillis() + "_" + i + "_" + j);
                    
                    // Add some GPS coordinates near the address
                    if (counter.getAddress().getLatitude() != null) {
                        reading.setLatitude(counter.getAddress().getLatitude() + (random.nextDouble() - 0.5) * 0.001);
                        reading.setLongitude(counter.getAddress().getLongitude() + (random.nextDouble() - 0.5) * 0.001);
                    }
                    
                    readings.add(reading);
                }
            }
        }
        
        // Batch save all readings at once
        if (!readings.isEmpty()) {
            readingRepository.saveAll(readings);
        }
    }
}