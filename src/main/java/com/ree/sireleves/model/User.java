package com.ree.sireleves.model;

// File: src/main/java/com/ree/sireleves/model/User.java
import jakarta.persistence.*;
import java.time.Instant;
import java.util.*;

@Entity
@Table(name = "users")
public class User {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 36)
    private String uuid; // UUID string

    @Column(name="last_name", nullable = false)
    private String lastName;

    @Column(name="first_name", nullable = false)
    private String firstName;

    @Column(nullable = false, unique = true)
    private String username; // email or login

    @Column(nullable = false)
    private String passwordHash;

    @Column(nullable = false)
    private boolean enabled = true;

    @Column(name = "must_change_password", nullable = false)
    private boolean mustChangePassword = false;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    @Column(name="created_at", nullable = false)
    private Instant createdAt = Instant.now();

    @Column(name="updated_at")
    private Instant updatedAt;

    @PreUpdate
    public void preUpdate(){ updatedAt = Instant.now(); }

    // getters & setters
    public Long getId(){return id;}
    public void setId(Long id){this.id = id;}
    public String getUuid(){return uuid;}
    public void setUuid(String uuid){this.uuid = uuid;}
    public String getLastName(){return lastName;}
    public void setLastName(String lastName){this.lastName = lastName;}
    public String getFirstName(){return firstName;}
    public void setFirstName(String firstName){this.firstName = firstName;}
    public String getUsername(){return username;}
    public void setUsername(String username){this.username = username;}
    public String getPasswordHash(){return passwordHash;}
    public void setPasswordHash(String passwordHash){this.passwordHash = passwordHash;}
    public boolean isEnabled(){return enabled;}
    public void setEnabled(boolean enabled){this.enabled = enabled;}
    public Set<Role> getRoles(){return roles;}
    public void setRoles(Set<Role> roles){this.roles = roles;}
    public boolean isMustChangePassword(){return mustChangePassword;}
    public void setMustChangePassword(boolean mustChangePassword){this.mustChangePassword = mustChangePassword;}
    public Instant getCreatedAt(){return createdAt;}
    public Instant getUpdatedAt(){return updatedAt;}
}

