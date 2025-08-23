package com.credtravels.flightsinfo.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;

@Entity
@Table(name = "flight_info")
public class FlightInfo {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "flight_number", nullable = false, length = 10)
    private String flightNumber;
    
    @Column(name = "airline_id", nullable = false)
    private Long airlineId;
    
    @Column(name = "aircraft_id")
    private Long aircraftId;
    
    @Column(name = "departure_airport_id", nullable = false)
    private Long departureAirportId;
    
    @Column(name = "arrival_airport_id", nullable = false)
    private Long arrivalAirportId;
    
    @Column(name = "departure_time", nullable = false)
    @JsonFormat(pattern = "HH:mm:ss")
    private LocalTime departureTime;
    
    @Column(name = "arrival_time", nullable = false)
    @JsonFormat(pattern = "HH:mm:ss")
    private LocalTime arrivalTime;
    
    @Column(name = "flight_duration_minutes", nullable = false)
    private Integer flightDurationMinutes;
    
    @Column(name = "operating_days", nullable = false, length = 7)
    private String operatingDays; // "1111100" (Mon-Sun)
    
    @Column(name = "effective_from")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate effectiveFrom;
    
    @Column(name = "effective_until")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate effectiveUntil;
    
    @Column(name = "services", columnDefinition = "JSON")
    private String services; // {"baggage": "20kg", "meal": "included", "wifi": true}
    
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private FlightStatus status;
    
    @Column(name = "created_at")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;
    
    // Constructors
    public FlightInfo() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.status = FlightStatus.ACTIVE;
    }
    
    public FlightInfo(String flightNumber, Long airlineId, Long departureAirportId, 
                     Long arrivalAirportId, LocalTime departureTime, LocalTime arrivalTime) {
        this();
        this.flightNumber = flightNumber;
        this.airlineId = airlineId;
        this.departureAirportId = departureAirportId;
        this.arrivalAirportId = arrivalAirportId;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getFlightNumber() {
        return flightNumber;
    }
    
    public void setFlightNumber(String flightNumber) {
        this.flightNumber = flightNumber;
    }
    
    public Long getAirlineId() {
        return airlineId;
    }
    
    public void setAirlineId(Long airlineId) {
        this.airlineId = airlineId;
    }
    
    public Long getAircraftId() {
        return aircraftId;
    }
    
    public void setAircraftId(Long aircraftId) {
        this.aircraftId = aircraftId;
    }
    
    public Long getDepartureAirportId() {
        return departureAirportId;
    }
    
    public void setDepartureAirportId(Long departureAirportId) {
        this.departureAirportId = departureAirportId;
    }
    
    public Long getArrivalAirportId() {
        return arrivalAirportId;
    }
    
    public void setArrivalAirportId(Long arrivalAirportId) {
        this.arrivalAirportId = arrivalAirportId;
    }
    
    public LocalTime getDepartureTime() {
        return departureTime;
    }
    
    public void setDepartureTime(LocalTime departureTime) {
        this.departureTime = departureTime;
    }
    
    public LocalTime getArrivalTime() {
        return arrivalTime;
    }
    
    public void setArrivalTime(LocalTime arrivalTime) {
        this.arrivalTime = arrivalTime;
    }
    
    public Integer getFlightDurationMinutes() {
        return flightDurationMinutes;
    }
    
    public void setFlightDurationMinutes(Integer flightDurationMinutes) {
        this.flightDurationMinutes = flightDurationMinutes;
    }
    
    public String getOperatingDays() {
        return operatingDays;
    }
    
    public void setOperatingDays(String operatingDays) {
        this.operatingDays = operatingDays;
    }
    
    public LocalDate getEffectiveFrom() {
        return effectiveFrom;
    }
    
    public void setEffectiveFrom(LocalDate effectiveFrom) {
        this.effectiveFrom = effectiveFrom;
    }
    
    public LocalDate getEffectiveUntil() {
        return effectiveUntil;
    }
    
    public void setEffectiveUntil(LocalDate effectiveUntil) {
        this.effectiveUntil = effectiveUntil;
    }
    
    public String getServices() {
        return services;
    }
    
    public void setServices(String services) {
        this.services = services;
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
    
    public boolean isOperatingOnDay(int dayOfWeek) {
        if (operatingDays == null || operatingDays.length() != 7) {
            return false;
        }
        return operatingDays.charAt(dayOfWeek - 1) == '1';
    }
    
    public enum FlightStatus {
        ACTIVE, SUSPENDED, CANCELLED
    }
}
