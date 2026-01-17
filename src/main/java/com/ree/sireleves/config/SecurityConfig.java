// File: src/main/java/com/ree/sireleves/config/SecurityConfig.java
package com.ree.sireleves.config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfigurationSource;

import com.ree.sireleves.security.JwtAuthenticationFilter;
import com.ree.sireleves.service.JwtService;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, JwtService jwtService, CorsConfigurationSource corsConfigurationSource) throws Exception {
        JwtAuthenticationFilter jwtFilter = new JwtAuthenticationFilter(jwtService);
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource))
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        // Health endpoints (public)
                        .requestMatchers("/api/health", "/api/mobile/health").permitAll()
                        // Data seeding endpoints (public for development)
                        .requestMatchers("/api/seed-test-data", "/api/clear-test-data").permitAll()
                        
                        // Mobile
                        .requestMatchers("/api/mobile/auth/**").permitAll()  // doit être ici !
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/api/mobile/**").hasRole("AGENT")

                        .requestMatchers("/h2-console/**").permitAll()
                        .requestMatchers("/api/admin/**").hasAuthority("SUPERADMIN")
                        .requestMatchers("/api/users/me").authenticated()
                        .requestMatchers("/api/users/change-password").authenticated()






                        // Batch Odoo
                        .requestMatchers("/api/batch/**").hasAnyRole("USER", "SUPERADMIN")

                        // Backoffice
                        .requestMatchers("/api/backoffice/**").hasAnyRole("USER", "SUPERADMIN")
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .sessionManagement(session -> session.disable()); // stateless
        // allow frames for H2 console in dev
        http.headers(headers -> headers.frameOptions(frame -> frame.sameOrigin()));
        return http.build();
    }
}
