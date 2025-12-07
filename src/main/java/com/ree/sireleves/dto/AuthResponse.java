package com.ree.sireleves.dto;

// File: src/main/java/com/ree/sireleves/dto/AuthResponse.java
public class AuthResponse {
    private String token;
    private long expiresInSeconds;
    public AuthResponse(String token, long expiresInSeconds){this.token = token; this.expiresInSeconds = expiresInSeconds;}
    public String getToken(){return token;}
    public long getExpiresInSeconds(){return expiresInSeconds;}
}
