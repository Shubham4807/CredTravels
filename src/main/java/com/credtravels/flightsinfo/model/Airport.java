package com.credtravels.flightsinfo.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "airports")
public class Airport {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "iata_code", unique = true, nullable = false, length = 3)
    private String iataCode;
    
    @Column(name = "icao_code", unique = true, length = 4)
    private String icaoCode;
    
    @Column(name = "name", nullable = false, length = 200)
    private String name;
    
    @Column(name = "city", nullable = false, length = 100)
    private String city;
    
    @Column(name = "country", nullable = false, length = 50)
    private String country;
    
    @Column(name = "timezone", length = 50)
    private String timezone;
    
    @Column(name = "latitude", precision = 10, scale = 8)
    private BigDecimal latitude;
    
    @Column(name = "longitude", precision = 11, scale = 8)
    private BigDecimal longitude;
    
    @Column(name = "elevation")
    private Integer elevation;
    
    @Column(name = "created_at")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
    
    // Constructors
    public Airport() {
        this.createdAt = LocalDateTime.now();
    }
    
    public Airport(String iataCode, String name, String city, String country) {
        this();
        this.iataCode = iataCode;
        this.name = name;
        this.city = city;
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
    
    public String getCity() {
        return city;
    }
    
    public void setCity(String city) {
        this.city = city;
    }
    
    public String getCountry() {
        return country;
    }
    
    public void setCountry(String country) {
        this.country = country;
    }
    
    public String getTimezone() {
        return timezone;
    }
    
    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }
    
    public BigDecimal getLatitude() {
        return latitude;
    }
    
    public void setLatitude(BigDecimal latitude) {
        this.latitude = latitude;
    }
    
    public BigDecimal getLongitude() {
        return longitude;
    }
    
    public void setLongitude(BigDecimal longitude) {
        this.longitude = longitude;
    }
    
    public Integer getElevation() {
        return elevation;
    }
    
    public void setElevation(Integer elevation) {
        this.elevation = elevation;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
