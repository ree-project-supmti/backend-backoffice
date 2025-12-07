// File: src/main/java/com/ree/sireleves/dto/UserCreateDTO.java
package com.ree.sireleves.dto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
public class UserCreateDTO {
    @NotBlank private String lastName;
    @NotBlank private String firstName;
    @NotBlank private String username; // email/login
    @NotNull private String role; // ROLE_SUPERADMIN or ROLE_USER
    // getters/setters
    public String getLastName(){return lastName;}
    public void setLastName(String lastName){this.lastName = lastName;}
    public String getFirstName(){return firstName;}
    public void setFirstName(String firstName){this.firstName = firstName;}
    public String getUsername(){return username;}
    public void setUsername(String username){this.username = username;}
    public String getRole(){return role;}
    public void setRole(String role){this.role = role;}
}
