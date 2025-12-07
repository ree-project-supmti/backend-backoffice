// File: src/main/java/com/ree/sireleves/dto/UserResponseDTO.java
package com.ree.sireleves.dto;
import java.time.Instant;
public class UserResponseDTO {
    private String uuid;
    private String lastName;
    private String firstName;
    private String username;
    private boolean enabled;
    private boolean mustChangePassword;
    private Instant createdAt;
    // getters/setters
    public String getUuid(){return uuid;}
    public void setUuid(String uuid){this.uuid = uuid;}
    public String getLastName(){return lastName;}
    public void setLastName(String lastName){this.lastName = lastName;}
    public String getFirstName(){return firstName;}
    public void setFirstName(String firstName){this.firstName = firstName;}
    public String getUsername(){return username;}
    public void setUsername(String username){this.username = username;}
    public boolean isEnabled(){return enabled;}
    public void setEnabled(boolean enabled){this.enabled = enabled;}
    public boolean isMustChangePassword(){return mustChangePassword;}
    public void setMustChangePassword(boolean mustChangePassword){this.mustChangePassword = mustChangePassword;}
    public Instant getCreatedAt(){return createdAt;}
    public void setCreatedAt(Instant createdAt){this.createdAt = createdAt;}
}
