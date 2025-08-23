package com.credtravels.inventory.dto;

import com.credtravels.inventory.model.SeatReservation.SeatClass;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;

public class SeatReservationRequest {
    
    @NotNull(message = "Seat class is required")
    private SeatClass seatClass;
    
    @NotNull(message = "Seat count is required")
    @Min(value = 1, message = "Minimum 1 seat required")
    @Max(value = 9, message = "Maximum 9 seats allowed")
    private Integer seatCount;
    
    private String customerId;
    
    // Constructors
    public SeatReservationRequest() {}
    
    public SeatReservationRequest(SeatClass seatClass, Integer seatCount) {
        this.seatClass = seatClass;
        this.seatCount = seatCount;
    }
    
    public SeatReservationRequest(SeatClass seatClass, Integer seatCount, String customerId) {
        this(seatClass, seatCount);
        this.customerId = customerId;
    }
    
    // Getters and Setters
    public SeatClass getSeatClass() {
        return seatClass;
    }
    
    public void setSeatClass(SeatClass seatClass) {
        this.seatClass = seatClass;
    }
    
    public Integer getSeatCount() {
        return seatCount;
    }
    
    public void setSeatCount(Integer seatCount) {
        this.seatCount = seatCount;
    }
    
    public String getCustomerId() {
        return customerId;
    }
    
    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }
}
