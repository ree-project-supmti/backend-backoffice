package com.ree.sireleves.repository;

import com.ree.sireleves.model.Reading;
import com.ree.sireleves.model.enums.ReadingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface ReadingRepository extends JpaRepository<Reading, Long> {

    Optional<Reading> findByMobileUuid(String mobileUuid);

    List<Reading> findByStatus(ReadingStatus status);

    @Query("""
        select r from Reading r
        where r.status = :status
          and r.readingDate >= :since
    """)
    List<Reading> findByStatusSince(
            @Param("status") ReadingStatus status,
            @Param("since") Instant since
    );

    @Query("""
    select count(r) from Reading r
    where r.agent.id = :agentId
      and r.readingDate >= :start
      and r.readingDate <= :end
""")
    long countByAgentAndPeriod(
            @Param("agentId") Long agentId,
            @Param("start") Instant start,
            @Param("end") Instant end
    );

    @Query("""
    select count(r) from Reading r
    where r.counter.address.district = :district
      and r.readingDate >= :start
      and r.readingDate <= :end
""")
    long countByDistrictAndPeriod(
            @Param("district") String district,
            @Param("start") Instant start,
            @Param("end") Instant end
    );

    @Query("""
    select count(r) from Reading r
    where r.counter.address.id = :addressId
      and r.status = com.ree.sireleves.model.enums.ReadingStatus.VALIDATED
""")
    int countReadByAddress(Long addressId);

    @Query("""
    select count(r) > 0 from Reading r
    where r.counter.id = :counterId
      and r.status = com.ree.sireleves.model.enums.ReadingStatus.VALIDATED
""")
    boolean existsValidatedReading(Long counterId);

}
