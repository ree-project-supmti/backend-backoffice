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
        System.out.println("🌱 Starting production-like data seeding...");

        // Clear existing data
        clearTestData();

        // Create larger dataset
        List<Client> clients = createProductionClients();
        List<Address> addresses = createProductionAddresses(clients);
        List<Counter> counters = createProductionCounters(addresses);
        createProductionReadings(counters);

        System.out.println("✅ Production-like data seeding completed!");
        System.out.println("📊 Created:");
        System.out.println("   - " + clients.size() + " clients");
        System.out.println("   - " + addresses.size() + " addresses");
        System.out.println("   - " + counters.size() + " counters");
        System.out.println("   - Historical readings for testing");
    }

    private void clearTestData() {
        readingRepository.deleteAll();
        counterRepository.deleteAll();
        addressRepository.deleteAll();
        clientRepository.deleteAll();
        System.out.println("🗑️ Cleared existing test data from MySQL");
    }

    private List<Client> createClients() {
        List<Client> clients = new ArrayList<>();
        
        String[] firstNames = {
            "Mohammed", "Fatima", "Ahmed", "Khadija", "Hassan",
            "Aicha", "Youssef", "Zineb", "Omar", "Salma",
            "Karim", "Nadia", "Rachid", "Samira", "Mehdi",
            "Laila", "Amine", "Houda", "Samir", "Malika"
        };
        
        String[] lastNames = {
            "Alami", "Benali", "Idrissi", "Fassi", "Tazi",
            "Benjelloun", "Chraibi", "Kettani", "Lahlou", "Mekouar",
            "Naciri", "Ouazzani", "Sefrioui", "Tounsi", "Zniber"
        };

        for (int i = 1; i <= 25; i++) {
            Client client = new Client();
            client.setOdooId((long) i);
            client.setName(firstNames[random.nextInt(firstNames.length)] + " " + lastNames[random.nextInt(lastNames.length)]);
            client.setPhone("+212 6" + String.format("%02d", random.nextInt(100)) + 
                           String.format("%02d", random.nextInt(100)) + 
                           String.format("%02d", random.nextInt(100)));
            
            clients.add(clientRepository.save(client));
        }
        
        return clients;
    }

    private List<Address> createAddresses(List<Client> clients) {
        List<Address> addresses = new ArrayList<>();
        
        String[] buildings = {
            "Résidence Al Amal", "Immeuble Yasmine", "Résidence Les Jardins",
            "Immeuble Riad", "Résidence Palmier", "Immeuble Souissi",
            "Résidence Océan", "Immeuble Hassan", "Résidence Agdal", "Immeuble Hay Riad"
        };
        
        String[] streets = {
            "Avenue Mohammed V", "Rue Patrice Lumumba", "Avenue Allal Ben Abdellah",
            "Rue Oued Fès", "Avenue Hassan II", "Rue Abou Faris Al Marini",
            "Avenue Al Marsa", "Rue Tanger", "Avenue Annakhil", "Rue Meknès"
        };

        for (int i = 0; i < clients.size(); i++) {
            Client client = clients.get(i);
            String building = buildings[i % buildings.length];
            String street = streets[i % streets.length];
            String district = i < 12 ? "Agdal" : "Hay Riad";
            
            Address address = new Address();
            address.setOdooId((long) (i + 1));
            address.setClient(client);
            address.setBuildingName(building);
            address.setApartmentNumber(String.valueOf((i % 5) + 1) + String.valueOf((i / 5) + 1));
            address.setStreet(street);
            address.setDistrict(district);
            address.setPostalCode("10" + String.format("%03d", 100 + i));
            address.setCity("Rabat");
            address.setFullAddress(building + ", Apt " + address.getApartmentNumber() + 
                                 ", " + street + ", " + district + ", Rabat");
            
            addresses.add(addressRepository.save(address));
        }
        
        return addresses;
    }

    private List<Counter> createCounters(List<Address> addresses) {
        List<Counter> counters = new ArrayList<>();
        
        for (Address address : addresses) {
            // Create water counter for each address
            Counter waterCounter = new Counter();
            waterCounter.setOdooId((long) (counters.size() + 1));
            waterCounter.setAddress(address);
            waterCounter.setType(CounterType.WATER);
            waterCounter.setCurrentIndex(1000.0 + random.nextDouble() * 5000);
            waterCounter.setLastReadingDate(LocalDateTime.now().minusDays(random.nextInt(60)));
            counters.add(counterRepository.save(waterCounter));
            
            // Create electricity counter for 80% of addresses
            if (random.nextDouble() < 0.8) {
                Counter elecCounter = new Counter();
                elecCounter.setOdooId((long) (counters.size() + 1));
                elecCounter.setAddress(address);
                elecCounter.setType(CounterType.ELECTRICITY);
                elecCounter.setCurrentIndex(5000.0 + random.nextDouble() * 20000);
                elecCounter.setLastReadingDate(LocalDateTime.now().minusDays(random.nextInt(60)));
                counters.add(counterRepository.save(elecCounter));
            }
        }
        
        return counters;
    }

    private void createReadings(List<Counter> counters) {
        // Create readings for 30% of counters
        int readingsToCreate = (int) (counters.size() * 0.3);
        
        for (int i = 0; i < readingsToCreate; i++) {
            Counter counter = counters.get(i);
            
            Reading reading = new Reading();
            reading.setCounter(counter);
            reading.setAgent(agentRepository.findByOdooId(1L).orElse(null)); // Test agent
            reading.setValue((int) (counter.getCurrentIndex() + 
                           (counter.getType() == CounterType.WATER ? 
                            random.nextDouble() * 20 + 5 : 
                            random.nextDouble() * 200 + 50)));
            reading.setReadingDate(LocalDateTime.now().minusDays(random.nextInt(30)).atZone(ZoneId.systemDefault()).toInstant());
            reading.setStatus(random.nextBoolean() ? ReadingStatus.VALIDATED : ReadingStatus.PENDING);
            reading.setMobileUuid("MOBILE_" + System.currentTimeMillis() + "_" + i);
            
            readingRepository.save(reading);
        }
    }

    public void clearAllData() {
        readingRepository.deleteAll();
        counterRepository.deleteAll();
        addressRepository.deleteAll();
        clientRepository.deleteAll();
        System.out.println("🗑️ Cleared all data from MySQL database");
    }

    private List<Client> createProductionClients() {
        List<Client> clients = new ArrayList<>();
        
        String[] firstNames = {
            "Mohammed", "Fatima", "Ahmed", "Khadija", "Hassan", "Aicha", "Youssef", "Zineb", 
            "Omar", "Salma", "Karim", "Nadia", "Rachid", "Samira", "Mehdi", "Laila", 
            "Amine", "Houda", "Samir", "Malika", "Abdelkader", "Latifa", "Mustapha", "Zahra",
            "Driss", "Amina", "Khalid", "Souad", "Brahim", "Naima", "Aziz", "Hafida",
            "Said", "Jamila", "Abderrahim", "Kenza", "Tarik", "Siham", "Noureddine", "Rajae"
        };
        
        String[] lastNames = {
            "Alami", "Benali", "Idrissi", "Fassi", "Tazi", "Benjelloun", "Chraibi", "Kettani", 
            "Lahlou", "Mekouar", "Naciri", "Ouazzani", "Sefrioui", "Tounsi", "Zniber",
            "Berrada", "Cherkaoui", "Filali", "Ghazi", "Hajji", "Ismaili", "Jazouli",
            "Kabbaj", "Lamrani", "Maârouf", "Nejjar", "Oufkir", "Qadiri", "Raissouni"
        };

        for (int i = 1; i <= 150; i++) {
            Client client = new Client();
            client.setOdooId((long) i);
            client.setName(firstNames[random.nextInt(firstNames.length)] + " " + lastNames[random.nextInt(lastNames.length)]);
            client.setPhone("+212 " + (random.nextBoolean() ? "6" : "5") + 
                           String.format("%02d", random.nextInt(100)) + 
                           String.format("%02d", random.nextInt(100)) + 
                           String.format("%02d", random.nextInt(100)));
            client.setCin(String.format("A%06d", random.nextInt(999999)));
            
            clients.add(clientRepository.save(client));
        }
        
        return clients;
    }

    private List<Address> createProductionAddresses(List<Client> clients) {
        List<Address> addresses = new ArrayList<>();
        
        String[] buildings = {
            "Résidence Al Amal", "Immeuble Yasmine", "Résidence Les Jardins", "Immeuble Riad",
            "Résidence Palmier", "Immeuble Souissi", "Résidence Océan", "Immeuble Hassan",
            "Résidence Agdal", "Immeuble Hay Riad", "Résidence Atlas", "Immeuble Anfa",
            "Résidence Majorelle", "Immeuble Gueliz", "Résidence Menara", "Immeuble Hivernage",
            "Résidence Palmeraie", "Immeuble Maarif", "Résidence Racine", "Immeuble Gauthier"
        };
        
        String[] streets = {
            "Avenue Mohammed V", "Rue Patrice Lumumba", "Avenue Allal Ben Abdellah", "Rue Oued Fès",
            "Avenue Hassan II", "Rue Abou Faris Al Marini", "Avenue Al Marsa", "Rue Tanger",
            "Avenue Annakhil", "Rue Meknès", "Boulevard Zerktouni", "Rue Ibn Sina", "Avenue Yacoub Al Mansour",
            "Rue Al Jazair", "Boulevard Mohammed VI", "Avenue Prince Héritier", "Rue Moulay Ismail",
            "Avenue des FAR", "Rue Abderrahmane Sahraoui", "Boulevard Al Massira"
        };

        String[] districts = {"Agdal", "Hay Riad", "Souissi", "Hassan", "Yacoub Al Mansour", "Takaddoum"};

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
            
            addresses.add(addressRepository.save(address));
        }
        
        return addresses;
    }

    private List<Counter> createProductionCounters(List<Address> addresses) {
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
            counters.add(counterRepository.save(waterCounter));
            
            // Create electricity counter for 85% of addresses
            if (random.nextDouble() < 0.85) {
                Counter elecCounter = new Counter();
                elecCounter.setOdooId(serialCounter);
                elecCounter.setSerialNumber(String.format("%09d", serialCounter++));
                elecCounter.setAddress(address);
                elecCounter.setClient(address.getClient());
                elecCounter.setType(CounterType.ELECTRICITY);
                elecCounter.setCurrentIndex(5000.0 + random.nextDouble() * 25000);
                elecCounter.setLastReadingDate(LocalDateTime.now().minusDays(random.nextInt(90)));
                elecCounter.setActive(true);
                counters.add(counterRepository.save(elecCounter));
            }
        }
        
        return counters;
    }

    private void createProductionReadings(List<Counter> counters) {
        // Create readings for 60% of counters with historical data
        int readingsToCreate = (int) (counters.size() * 0.6);
        
        for (int i = 0; i < readingsToCreate; i++) {
            Counter counter = counters.get(i);
            Agent testAgent = agentRepository.findByOdooId(1L).orElse(null);
            
            if (testAgent != null) {
                // Create 2-3 historical readings per counter
                int numReadings = 2 + random.nextInt(2);
                
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
                                         .minusDays(90 - (j * 30) + random.nextInt(10))
                                         .atZone(ZoneId.systemDefault()).toInstant());
                    
                    // Vary status distribution
                    ReadingStatus[] statuses = {ReadingStatus.PENDING, ReadingStatus.VALIDATED, ReadingStatus.SENT};
                    reading.setStatus(statuses[random.nextInt(statuses.length)]);
                    
                    reading.setMobileUuid("MOBILE_" + System.currentTimeMillis() + "_" + i + "_" + j);
                    
                    // Add some GPS coordinates near the address
                    if (counter.getAddress().getLatitude() != null) {
                        reading.setLatitude(counter.getAddress().getLatitude() + (random.nextDouble() - 0.5) * 0.001);
                        reading.setLongitude(counter.getAddress().getLongitude() + (random.nextDouble() - 0.5) * 0.001);
                    }
                    
                    readingRepository.save(reading);
                }
            }
        }
    }
}