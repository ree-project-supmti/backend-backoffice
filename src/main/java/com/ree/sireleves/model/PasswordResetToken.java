package com.ree.sireleves.model;

// File: src/main/java/com/ree/sireleves/model/PasswordResetToken.java
import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name="password_reset_tokens")
public class PasswordResetToken {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable=false, unique=true)
    private String token;

    @Column(nullable=false)
    private Instant expiresAt;

    @Column(nullable=false)
    private boolean used = false;

    // getters/setters
    public Long getId(){return id;}
    public void setId(Long id){this.id = id;}
    public User getUser(){return user;}
    public void setUser(User user){this.user = user;}
    public String getToken(){return token;}
    public void setToken(String token){this.token = token;}
    public Instant getExpiresAt(){return expiresAt;}
    public void setExpiresAt(Instant expiresAt){this.expiresAt = expiresAt;}
    public boolean isUsed(){return used;}
    public void setUsed(boolean used){this.used = used;}
}
