// File: src/main/java/com/ree/sireleves/config/DataInitializer.java
package com.ree.sireleves.config;
import java.util.UUID;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.ree.sireleves.model.Agent;
import com.ree.sireleves.model.Role;
import com.ree.sireleves.model.User;
import com.ree.sireleves.repository.AgentRepository;
import com.ree.sireleves.repository.RoleRepository;
import com.ree.sireleves.repository.UserRepository;

@Component
public class DataInitializer implements CommandLineRunner {
    private final RoleRepository roleRepo;
    private final UserRepository userRepo;
    private final AgentRepository agentRepo;
    private final PasswordEncoder passwordEncoder;
    
    public DataInitializer(
            RoleRepository roleRepo, 
            UserRepository userRepo, 
            AgentRepository agentRepo,
            PasswordEncoder passwordEncoder
    ){
        this.roleRepo = roleRepo; 
        this.userRepo = userRepo; 
        this.agentRepo = agentRepo;
        this.passwordEncoder = passwordEncoder;
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
        
        // Create test agent for mobile app development
        Agent testAgent = agentRepo.findByOdooId(1L).orElse(null);
        if (testAgent == null) {
            Agent agent = new Agent();
            agent.setOdooId(1L);
            agent.setFirstName("Mohammed");
            agent.setLastName("ALAMI");
            agent.setPhone("+212612345678");
            agent.setDistrict("Agdal");
            agent.setSecretCode(passwordEncoder.encode("123456")); // Hash the PIN
            agent.setActive(true);
            agentRepo.save(agent);
            System.out.println("Created test agent: Mohammed ALAMI with PIN 123456 for mobile development!");
        } else {
            // Update existing agent's PIN if needed
            testAgent.setSecretCode(passwordEncoder.encode("123456"));
            testAgent.setActive(true);
            agentRepo.save(testAgent);
            System.out.println("Updated test agent PIN to 123456 for mobile development!");
        }
    }

    private Role createRole(String name){ Role r = new Role(); r.setName(name); return r; }
}
