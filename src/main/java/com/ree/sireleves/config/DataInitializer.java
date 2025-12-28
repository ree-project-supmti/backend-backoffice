// File: src/main/java/com/ree/sireleves/config/DataInitializer.java
package com.ree.sireleves.config;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import com.ree.sireleves.repository.RoleRepository;
import com.ree.sireleves.repository.UserRepository;
import com.ree.sireleves.model.Role;
import com.ree.sireleves.model.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.Optional;
import java.util.UUID;

@Component
public class DataInitializer implements CommandLineRunner {
    private final RoleRepository roleRepo;
    private final UserRepository userRepo;
    private final PasswordEncoder passwordEncoder;
    public DataInitializer(RoleRepository roleRepo, UserRepository userRepo, PasswordEncoder passwordEncoder){
        this.roleRepo = roleRepo; this.userRepo = userRepo; this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        Role r1 = roleRepo.findByName("ROLE_SUPERADMIN").orElseGet(() -> roleRepo.save(createRole("ROLE_SUPERADMIN")));
        Role r2 = roleRepo.findByName("ROLE_USER").orElseGet(() -> roleRepo.save(createRole("ROLE_USER")));

        User superadmin = userRepo.findByUsername("admin@ree.local").orElse(null);
        if(superadmin == null){
            // créer superadmin
            User u = new User();
            u.setUuid(UUID.randomUUID().toString());
            u.setFirstName("Admin");
            u.setLastName("REE");
            u.setUsername("admin@ree.local");
            u.setPasswordHash(passwordEncoder.encode("Admin123"));
            u.setMustChangePassword(true);
            u.getRoles().add(r1);
            userRepo.save(u);
            System.out.println("Created default superadmin: admin@ree.local / Admin123!");
        } else {
            // optionnel : reset password si nécessaire
            if(superadmin.isMustChangePassword()){
                superadmin.setPasswordHash(passwordEncoder.encode("Admin123"));
                superadmin.setMustChangePassword(true);
                userRepo.save(superadmin);
                System.out.println("Reset password for superadmin: admin@ree.local / Admin123!");
            }
        }
    }

    private Role createRole(String name){ Role r = new Role(); r.setName(name); return r; }
}
