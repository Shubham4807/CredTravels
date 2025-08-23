package com.credtravels.booking.dto;

import com.credtravels.booking.model.Booking.SeatClass;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;
import java.util.List;

public class BookingModificationRequest {
    
    private SeatClass seatClass;
    private String specialRequests;
    private List<BookingRequest.PassengerDetail> passengers;
    
    // Constructors
    public BookingModificationRequest() {}
    
    public BookingModificationRequest(SeatClass seatClass, String specialRequests, List<BookingRequest.PassengerDetail> passengers) {
        this.seatClass = seatClass;
        this.specialRequests = specialRequests;
        this.passengers = passengers;
    }
    
    // Getters and Setters
    public SeatClass getSeatClass() {
        return seatClass;
    }
    
    public void setSeatClass(SeatClass seatClass) {
        this.seatClass = seatClass;
    }
    
    public String getSpecialRequests() {
        return specialRequests;
    }
    
    public void setSpecialRequests(String specialRequests) {
        this.specialRequests = specialRequests;
    }
    
    public List<BookingRequest.PassengerDetail> getPassengers() {
        return passengers;
    }
    
    public void setPassengers(List<BookingRequest.PassengerDetail> passengers) {
        this.passengers = passengers;
    }
}
