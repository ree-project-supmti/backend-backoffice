package com.ree.sireleves.repository.core;

import com.ree.sireleves.model.core.Client;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClientRepository extends JpaRepository<Client, Long> {

    Optional<Client> findByOdooId(Long odooId);

    boolean existsByOdooId(Long odooId);
}
