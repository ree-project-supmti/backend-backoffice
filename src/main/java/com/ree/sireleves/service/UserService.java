// File: src/main/java/com/ree/sireleves/service/UserService.java
package com.ree.sireleves.service;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ree.sireleves.repository.UserRepository;
import com.ree.sireleves.repository.RoleRepository;
import com.ree.sireleves.model.User;
import com.ree.sireleves.model.Role;
import com.ree.sireleves.dto.UserCreateDTO;
import com.ree.sireleves.dto.UserResponseDTO;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.*;
import java.util.stream.Collectors;
import java.util.UUID;
import jakarta.persistence.EntityNotFoundException;

@Service
public class UserService {
    private final UserRepository userRepo;
    private final RoleRepository roleRepo;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepo, RoleRepository roleRepo, PasswordEncoder passwordEncoder){
        this.userRepo = userRepo;
        this.roleRepo = roleRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public UserResponseDTO createUser(UserCreateDTO dto){
        if(userRepo.existsByUsername(dto.getUsername())){
            throw new IllegalArgumentException("username already exists");
        }
        User u = new User();
        u.setUuid(UUID.randomUUID().toString());
        u.setLastName(dto.getLastName());
        u.setFirstName(dto.getFirstName());
        u.setUsername(dto.getUsername());
        // generate random password (8 chars) and mark mustChangePassword
        String clearPassword = generateRandomPassword(10);
        u.setPasswordHash(passwordEncoder.encode(clearPassword));
        u.setMustChangePassword(true);
        Role role = roleRepo.findByName(dto.getRole()).orElseThrow(()-> new EntityNotFoundException("Role not found"));
        u.getRoles().add(role);
        userRepo.save(u);

        // in real implementation: send email with clearPassword (use MailHog dev)
        UserResponseDTO out = mapToDto(u);
        // for demo: we attach password as username+":<password>"? Instead we will log it server-side (not here)
        return out;
    }

    public List<UserResponseDTO> listAll(){
        return userRepo.findAll().stream().map(this::mapToDto).collect(Collectors.toList());
    }

    public UserResponseDTO findByUuid(String uuid){
        return userRepo.findByUuid(uuid).map(this::mapToDto).orElseThrow(()-> new EntityNotFoundException("User not found"));
    }

    @Transactional
    public void resetPasswordForUser(String uuid){
        User u = userRepo.findByUuid(uuid).orElseThrow(()-> new EntityNotFoundException("User not found"));
        String randomPwd = generateRandomPassword(10);
        u.setPasswordHash(passwordEncoder.encode(randomPwd));
        u.setMustChangePassword(true);
        userRepo.save(u);
        // send mail with randomPwd (omitted). Log for dev.
    }

    private UserResponseDTO mapToDto(User u){
        UserResponseDTO d = new UserResponseDTO();
        d.setUuid(u.getUuid());
        d.setLastName(u.getLastName());
        d.setFirstName(u.getFirstName());
        d.setUsername(u.getUsername());
        d.setEnabled(u.isEnabled());
        d.setMustChangePassword(u.isMustChangePassword());
        d.setCreatedAt(u.getCreatedAt());
        return d;
    }

    private String generateRandomPassword(int len){
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789@#$%";
        Random rnd = new Random();
        StringBuilder sb = new StringBuilder(len);
        for(int i=0;i<len;i++) sb.append(chars.charAt(rnd.nextInt(chars.length())));
        return sb.toString();
    }
}
