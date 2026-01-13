package com.ree.sireleves.model.core;

import jakarta.persistence.*;

@Entity
@Table(name = "addresses")
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private Long odooId;

    private String street;
    private String city;
    private String district; // quartier (clé métier pour affectation agents)
    private String buildingName;
    private String apartmentNumber;
    private String postalCode;

    private String apartment;
    private String building;
    private String residence;

    private Double latitude;
    private Double longitude;

    @ManyToOne(optional = false)
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;


    @Transient
    private String fullAddress;

    @Transient
    public String getFullAddress() {
        if (fullAddress != null) {
            return fullAddress;
        }
        return String.join(", ",
                buildingName != null ? buildingName : "",
                apartmentNumber != null ? "Apt " + apartmentNumber : "",
                street != null ? street : "",
                district != null ? district : "",
                city != null ? city : ""
        ).replaceAll("^, |, $|, , ", ", ").replaceAll("^, |, $", "");
    }

    public void setFullAddress(String fullAddress) {
        this.fullAddress = fullAddress;
    }

    // getters/setters

    public Long getOdooId() {
        return odooId;
    }

    public void setOdooId(Long odooId) {
        this.odooId = odooId;
    }

    public String getBuildingName() {
        return buildingName;
    }

    public void setBuildingName(String buildingName) {
        this.buildingName = buildingName;
    }

    public String getApartmentNumber() {
        return apartmentNumber;
    }

    public void setApartmentNumber(String apartmentNumber) {
        this.apartmentNumber = apartmentNumber;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getApartment() {
        return apartment;
    }

    public void setApartment(String apartment) {
        this.apartment = apartment;
    }

    public String getBuilding() {
        return building;
    }

    public void setBuilding(String building) {
        this.building = building;
    }

    public String getResidence() {
        return residence;
    }

    public void setResidence(String residence) {
        this.residence = residence;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }
}
