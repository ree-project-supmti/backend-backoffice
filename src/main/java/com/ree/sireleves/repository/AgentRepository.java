package com.ree.sireleves.repository;

import com.ree.sireleves.model.Agent;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AgentRepository extends JpaRepository<Agent, Long> {

    Optional<Agent> findByOdooId(Long odooId);

   // Optional<Agent> findByUserId(Long userId);

    List<Agent> findByDistrictAndActiveTrue(String district);
    
    List<Agent> findByActiveTrue();

    // Pagination support
    Page<Agent> findByActiveTrue(Pageable pageable);
    
    Page<Agent> findByDistrictAndActiveTrue(String district, Pageable pageable);

    boolean existsByOdooId(Long odooId);
    Optional<Agent> findBySecretCode(String secretCode);
}
