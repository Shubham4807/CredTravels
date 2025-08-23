package com.credtravels.search.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;

import java.time.LocalDate;

public class FlightSearchRequest {
    
    @NotBlank(message = "Departure airport is required")
    private String from;
    
    @NotBlank(message = "Arrival airport is required")
    private String to;
    
    @NotNull(message = "Departure date is required")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;
    
    @Min(value = 1, message = "Max hops must be at least 1")
    @Max(value = 3, message = "Max hops cannot exceed 3")
    private Integer maxHops = 1;
    
    @Min(value = 1, message = "Result limit must be at least 1")
    @Max(value = 100, message = "Result limit cannot exceed 100")
    private Integer limit = 50;
    
    private String seatClass;
    private Integer passengers = 1;
    
    // Constructors
    public FlightSearchRequest() {}
    
    public FlightSearchRequest(String from, String to, LocalDate date) {
        this.from = from;
        this.to = to;
        this.date = date;
    }
    
    // Getters and Setters
    public String getFrom() {
        return from;
    }
    
    public void setFrom(String from) {
        this.from = from;
    }
    
    public String getTo() {
        return to;
    }
    
    public void setTo(String to) {
        this.to = to;
    }
    
    public LocalDate getDate() {
        return date;
    }
    
    public void setDate(LocalDate date) {
        this.date = date;
    }
    
    public Integer getMaxHops() {
        return maxHops;
    }
    
    public void setMaxHops(Integer maxHops) {
        this.maxHops = maxHops;
    }
    
    public Integer getLimit() {
        return limit;
    }
    
    public void setLimit(Integer limit) {
        this.limit = limit;
    }
    
    public String getSeatClass() {
        return seatClass;
    }
    
    public void setSeatClass(String seatClass) {
        this.seatClass = seatClass;
    }
    
    public Integer getPassengers() {
        return passengers;
    }
    
    public void setPassengers(Integer passengers) {
        this.passengers = passengers;
    }
}
