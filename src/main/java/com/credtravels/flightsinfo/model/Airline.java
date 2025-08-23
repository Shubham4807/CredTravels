package com.credtravels.flightsinfo.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "airlines")
public class Airline {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "iata_code", unique = true, nullable = false, length = 3)
    private String iataCode;
    
    @Column(name = "icao_code", unique = true, length = 4)
    private String icaoCode;
    
    @Column(name = "name", nullable = false, length = 100)
    private String name;
    
    @Column(name = "country", length = 50)
    private String country;
    
    @Column(name = "logo_url", length = 500)
    private String logoUrl;
    
    @Column(name = "rating", precision = 3, scale = 2)
    private BigDecimal rating;
    
    @Column(name = "services", columnDefinition = "JSON")
    private String services; // JSON string: {"wifi": true, "meal": true, "entertainment": true}
    
    @Column(name = "created_at")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;
    
    // Constructors
    public Airline() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
    
    public Airline(String iataCode, String name, String country) {
        this();
        this.iataCode = iataCode;
        this.name = name;
        this.country = country;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getIataCode() {
        return iataCode;
    }
    
    public void setIataCode(String iataCode) {
        this.iataCode = iataCode;
    }
    
    public String getIcaoCode() {
        return icaoCode;
    }
    
    public void setIcaoCode(String icaoCode) {
        this.icaoCode = icaoCode;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getCountry() {
        return country;
    }
    
    public void setCountry(String country) {
        this.country = country;
    }
    
    public String getLogoUrl() {
        return logoUrl;
    }
    
    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }
    
    public BigDecimal getRating() {
        return rating;
    }
    
    public void setRating(BigDecimal rating) {
        this.rating = rating;
    }
    
    public String getServices() {
        return services;
    }
    
    public void setServices(String services) {
        this.services = services;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    // Business methods
    public void updateTimestamp() {
        this.updatedAt = LocalDateTime.now();
    }
}
