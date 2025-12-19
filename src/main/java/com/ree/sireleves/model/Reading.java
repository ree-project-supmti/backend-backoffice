package com.ree.sireleves.model;

import com.ree.sireleves.model.Agent;
import com.ree.sireleves.model.core.Counter;
import com.ree.sireleves.model.enums.ReadingStatus;
import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(
        name = "readings",
        indexes = {
                @Index(name = "idx_reading_counter", columnList = "counter_id"),
                @Index(name = "idx_reading_agent", columnList = "agent_id"),
                @Index(name = "idx_reading_status", columnList = "status"),
                @Index(name = "idx_reading_mobile_uuid", columnList = "mobileUuid")
        }
)
public class Reading {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "counter_id")
    private Counter counter;

    @ManyToOne(optional = false)
    @JoinColumn(name = "agent_id")
    private Agent agent;

    @Column(nullable = false)
    private Integer value; // index relevé

    @Column(nullable = false)
    private Instant readingDate;

    private Double latitude;
    private Double longitude;

    private String photoUrl;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReadingStatus status = ReadingStatus.PENDING;

    private Instant sentAt;

    @Column(nullable = false, unique = true)
    private String mobileUuid; // gestion offline mobile

    @Column(nullable = false)
    private Instant createdAt = Instant.now();

    // getters/setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Counter getCounter() {
        return counter;
    }

    public void setCounter(Counter counter) {
        this.counter = counter;
    }

    public Agent getAgent() {
        return agent;
    }

    public void setAgent(Agent agent) {
        this.agent = agent;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public Instant getReadingDate() {
        return readingDate;
    }

    public void setReadingDate(Instant readingDate) {
        this.readingDate = readingDate;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public ReadingStatus getStatus() {
        return status;
    }

    public void setStatus(ReadingStatus status) {
        this.status = status;
    }

    public Instant getSentAt() {
        return sentAt;
    }

    public void setSentAt(Instant sentAt) {
        this.sentAt = sentAt;
    }

    public String getMobileUuid() {
        return mobileUuid;
    }

    public void setMobileUuid(String mobileUuid) {
        this.mobileUuid = mobileUuid;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }
}
