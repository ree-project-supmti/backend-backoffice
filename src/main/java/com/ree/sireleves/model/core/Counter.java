package com.ree.sireleves.model.core;

import com.ree.sireleves.model.enums.CounterType;
import jakarta.persistence.*;

@Entity
@Table(
        name = "counters",
        indexes = {
                @Index(name = "idx_counter_serial", columnList = "serialNumber"),
                @Index(name = "idx_counter_odoo", columnList = "odooId")
        }
)
public class Counter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String serialNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CounterType type;

    @Column(nullable = false, unique = true)
    private Long odooId;

    @ManyToOne(optional = false)
    @JoinColumn(name = "client_id")
    private Client client;

    @ManyToOne
    @JoinColumn(name = "address_id")
    private Address address;

    private Boolean active = true;

    // getters/setters


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public CounterType getType() {
        return type;
    }

    public void setType(CounterType type) {
        this.type = type;
    }

    public Long getOdooId() {
        return odooId;
    }

    public void setOdooId(Long odooId) {
        this.odooId = odooId;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}
