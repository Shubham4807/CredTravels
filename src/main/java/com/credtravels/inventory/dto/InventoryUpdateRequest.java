package com.credtravels.inventory.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

public class InventoryUpdateRequest {
    
    @NotNull(message = "Flight date is required")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate flightDate;
    
    @NotNull(message = "Available seats are required")
    private Map<String, Integer> availableSeats;
    
    @NotNull(message = "Pricing is required")
    private Map<String, BigDecimal> pricing;
    
    @NotNull(message = "Total capacity is required")
    private Map<String, Integer> totalCapacity;
    
    private String updateReason;
    
    private String updatedBy;
    
    // Constructors
    public InventoryUpdateRequest() {}
    
    public InventoryUpdateRequest(LocalDate flightDate, Map<String, Integer> availableSeats, 
                                Map<String, BigDecimal> pricing, Map<String, Integer> totalCapacity) {
        this.flightDate = flightDate;
        this.availableSeats = availableSeats;
        this.pricing = pricing;
        this.totalCapacity = totalCapacity;
    }
    
    // Getters and Setters
    public LocalDate getFlightDate() {
        return flightDate;
    }
    
    public void setFlightDate(LocalDate flightDate) {
        this.flightDate = flightDate;
    }
    
    public Map<String, Integer> getAvailableSeats() {
        return availableSeats;
    }
    
    public void setAvailableSeats(Map<String, Integer> availableSeats) {
        this.availableSeats = availableSeats;
    }
    
    public Map<String, BigDecimal> getPricing() {
        return pricing;
    }
    
    public void setPricing(Map<String, BigDecimal> pricing) {
        this.pricing = pricing;
    }
    
    public Map<String, Integer> getTotalCapacity() {
        return totalCapacity;
    }
    
    public void setTotalCapacity(Map<String, Integer> totalCapacity) {
        this.totalCapacity = totalCapacity;
    }
    
    public String getUpdateReason() {
        return updateReason;
    }
    
    public void setUpdateReason(String updateReason) {
        this.updateReason = updateReason;
    }
    
    public String getUpdatedBy() {
        return updatedBy;
    }
    
    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }
}
