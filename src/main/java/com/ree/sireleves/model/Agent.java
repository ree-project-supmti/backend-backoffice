package com.ree.sireleves.model;

import com.ree.sireleves.model.User;
import jakarta.persistence.*;

@Entity
@Table(
        name = "agents",
        indexes = {
                @Index(name = "idx_agent_odoo", columnList = "odooId"),
                @Index(name = "idx_agent_district", columnList = "district")
        }
)
public class Agent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private Long odooId;

    private String firstName;
    private String lastName;
    private String phone;

    @Column(nullable = false, length = 6)
    private String secretCode;

    @Column(nullable = false)
    private String district; // quartier affecté

   /* @OneToOne(optional = false)
    @JoinColumn(name = "user_id")
    private User user; // login mobile / sécurité*/

    private Boolean active = true;

    // getters/setters


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOdooId() {
        return odooId;
    }

    public void setOdooId(Long odooId) {
        this.odooId = odooId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    /*public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }*/

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}
