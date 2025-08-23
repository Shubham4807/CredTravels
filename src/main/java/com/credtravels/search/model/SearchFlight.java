package com.credtravels.search.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.time.LocalDateTime;

@Entity
@Table(name = "search_flight")
public class SearchFlight {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "flight_info_id", nullable = false)
    private Long flightInfoId;
    
    @Column(name = "flight_number", nullable = false, length = 10)
    private String flightNumber;
    
    @Column(name = "airline_name", length = 100)
    private String airlineName;
    
    @Column(name = "departure_airport_code", length = 3)
    private String departureAirportCode;
    
    @Column(name = "departure_city", length = 100)
    private String departureCity;
    
    @Column(name = "arrival_airport_code", length = 3)
    private String arrivalAirportCode;
    
    @Column(name = "arrival_city", length = 100)
    private String arrivalCity;
    
    @Column(name = "departure_time")
    @JsonFormat(pattern = "HH:mm:ss")
    private LocalTime departureTime;
    
    @Column(name = "arrival_time")
    @JsonFormat(pattern = "HH:mm:ss")
    private LocalTime arrivalTime;
    
    @Column(name = "duration_minutes")
    private Integer durationMinutes;
    
    @Column(name = "operating_days", length = 7)
    private String operatingDays;
    
    @Column(name = "base_price", precision = 10, scale = 2)
    private BigDecimal basePrice;
    
    @Column(name = "aircraft_model", length = 50)
    private String aircraftModel;
    
    @Column(name = "services", columnDefinition = "JSON")
    private String services;
    
    @Column(name = "route_popularity_score")
    private Integer routePopularityScore;
    
    @Column(name = "updated_at")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;
    
    // Constructors
    public SearchFlight() {
        this.updatedAt = LocalDateTime.now();
    }
    
    public SearchFlight(Long flightInfoId, String flightNumber, String airlineName) {
        this();
        this.flightInfoId = flightInfoId;
        this.flightNumber = flightNumber;
        this.airlineName = airlineName;
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
    
    public String getFlightNumber() {
        return flightNumber;
    }
    
    public void setFlightNumber(String flightNumber) {
        this.flightNumber = flightNumber;
    }
    
    public String getAirlineName() {
        return airlineName;
    }
    
    public void setAirlineName(String airlineName) {
        this.airlineName = airlineName;
    }
    
    public String getDepartureAirportCode() {
        return departureAirportCode;
    }
    
    public void setDepartureAirportCode(String departureAirportCode) {
        this.departureAirportCode = departureAirportCode;
    }
    
    public String getDepartureCity() {
        return departureCity;
    }
    
    public void setDepartureCity(String departureCity) {
        this.departureCity = departureCity;
    }
    
    public String getArrivalAirportCode() {
        return arrivalAirportCode;
    }
    
    public void setArrivalAirportCode(String arrivalAirportCode) {
        this.arrivalAirportCode = arrivalAirportCode;
    }
    
    public String getArrivalCity() {
        return arrivalCity;
    }
    
    public void setArrivalCity(String arrivalCity) {
        this.arrivalCity = arrivalCity;
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
    
    public Integer getDurationMinutes() {
        return durationMinutes;
    }
    
    public void setDurationMinutes(Integer durationMinutes) {
        this.durationMinutes = durationMinutes;
    }
    
    public String getOperatingDays() {
        return operatingDays;
    }
    
    public void setOperatingDays(String operatingDays) {
        this.operatingDays = operatingDays;
    }
    
    public BigDecimal getBasePrice() {
        return basePrice;
    }
    
    public void setBasePrice(BigDecimal basePrice) {
        this.basePrice = basePrice;
    }
    
    public String getAircraftModel() {
        return aircraftModel;
    }
    
    public void setAircraftModel(String aircraftModel) {
        this.aircraftModel = aircraftModel;
    }
    
    public String getServices() {
        return services;
    }
    
    public void setServices(String services) {
        this.services = services;
    }
    
    public Integer getRoutePopularityScore() {
        return routePopularityScore;
    }
    
    public void setRoutePopularityScore(Integer routePopularityScore) {
        this.routePopularityScore = routePopularityScore;
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
