package com.credtravels.inventory.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import org.hibernate.annotations.Type;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;

@Entity
@Table(name = "flight_inventory")
public class FlightInventory {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "flight_info_id", nullable = false)
    private Long flightInfoId;
    
    @Column(name = "flight_date", nullable = false)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate flightDate;
    
    @Column(name = "available_seats", columnDefinition = "JSON")
    private String availableSeats; // JSON string: {"economy": 150, "business": 20, "first": 8}
    
    @Column(name = "pricing", columnDefinition = "JSON")
    private String pricing; // JSON string: {"economy": 15000, "business": 45000, "first": 80000}
    
    @Column(name = "total_capacity", columnDefinition = "JSON")
    private String totalCapacity; // JSON string: {"economy": 150, "business": 20, "first": 8}
    
    @Column(name = "last_updated")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime lastUpdated;
    
    @Version
    private Long version;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private FlightStatus status;
    
    @Column(name = "created_at")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
    
    // Constructors
    public FlightInventory() {
        this.createdAt = LocalDateTime.now();
        this.lastUpdated = LocalDateTime.now();
        this.status = FlightStatus.ACTIVE;
        this.version = 1L;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Long getFlightInfoId() {
        return flightInfoId;
    }
    
    public void setFlightInfoId(Long flightInfoId) {
        this.flightInfoId = flightInfoId;
    }
    
    public LocalDate getFlightDate() {
        return flightDate;
    }
    
    public void setFlightDate(LocalDate flightDate) {
        this.flightDate = flightDate;
    }
    
    public String getAvailableSeats() {
        return availableSeats;
    }
    
    public void setAvailableSeats(String availableSeats) {
        this.availableSeats = availableSeats;
    }
    
    public String getPricing() {
        return pricing;
    }
    
    public void setPricing(String pricing) {
        this.pricing = pricing;
    }
    
    public String getTotalCapacity() {
        return totalCapacity;
    }
    
    public void setTotalCapacity(String totalCapacity) {
        this.totalCapacity = totalCapacity;
    }
    
    public LocalDateTime getLastUpdated() {
        return lastUpdated;
    }
    
    public void setLastUpdated(LocalDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
    }
    
    public Long getVersion() {
        return version;
    }
    
    public void setVersion(Long version) {
        this.version = version;
    }
    
    public FlightStatus getStatus() {
        return status;
    }
    
    public void setStatus(FlightStatus status) {
        this.status = status;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    // Business methods
    public void updateLastUpdated() {
        this.lastUpdated = LocalDateTime.now();
    }
    
    public enum FlightStatus {
        ACTIVE, CANCELLED, DELAYED
    }
}
