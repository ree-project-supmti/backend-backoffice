package com.ree.sireleves.repository.core;

import com.ree.sireleves.model.core.Address;
import com.ree.sireleves.model.core.Counter;
import com.ree.sireleves.model.enums.CounterType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CounterRepository extends JpaRepository<Counter, Long> {

    Optional<Counter> findBySerialNumber(String serialNumber);

    Optional<Counter> findByOdooId(Long odooId);

    List<Counter> findByClientId(Long clientId);

    @Query("""
        select c from Counter c
        where c.active = true
          and c.address.district = :district
    """)
    List<Counter> findActiveCountersByDistrict(String district);

    @Query("""
        select count(c) from Counter c
        where c.active = true
          and c.address.district = :district
    """)
    long countActiveByDistrict(String district);

    long countByType(CounterType type);


    //List<Counter> findByDistrict(String district);

    List<Counter> findByAddress_District(String district);


    int countByAddress(Address address);

}
