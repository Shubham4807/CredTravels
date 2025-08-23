package com.credtravels.booking.dto;

import com.credtravels.booking.model.Booking.SeatClass;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class BookingRequest {
    
    @NotNull(message = "Flight info ID is required")
    private Long flightInfoId;
    
    @NotNull(message = "Flight date is required")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate flightDate;
    
    @NotNull(message = "Seat class is required")
    private SeatClass seatClass;
    
    @NotNull(message = "Passenger count is required")
    @Min(value = 1, message = "Minimum 1 passenger required")
    @Max(value = 9, message = "Maximum 9 passengers allowed")
    private Integer passengerCount;
    
    @NotNull(message = "Total amount is required")
    private BigDecimal totalAmount;
    
    private String specialRequests;
    private String customerId;
    
    @NotNull(message = "Passenger details are required")
    private List<PassengerDetail> passengers;
    
    // Constructors
    public BookingRequest() {}
    
    public BookingRequest(Long flightInfoId, LocalDate flightDate, SeatClass seatClass, 
                         Integer passengerCount, BigDecimal totalAmount) {
        this.flightInfoId = flightInfoId;
        this.flightDate = flightDate;
        this.seatClass = seatClass;
        this.passengerCount = passengerCount;
        this.totalAmount = totalAmount;
    }
    
    // Getters and Setters
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
    
    public SeatClass getSeatClass() {
        return seatClass;
    }
    
    public void setSeatClass(SeatClass seatClass) {
        this.seatClass = seatClass;
    }
    
    public Integer getPassengerCount() {
        return passengerCount;
    }
    
    public void setPassengerCount(Integer passengerCount) {
        this.passengerCount = passengerCount;
    }
    
    public BigDecimal getTotalAmount() {
        return totalAmount;
    }
    
    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }
    
    public String getSpecialRequests() {
        return specialRequests;
    }
    
    public void setSpecialRequests(String specialRequests) {
        this.specialRequests = specialRequests;
    }
    
    public String getCustomerId() {
        return customerId;
    }
    
    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }
    
    public List<PassengerDetail> getPassengers() {
        return passengers;
    }
    
    public void setPassengers(List<PassengerDetail> passengers) {
        this.passengers = passengers;
    }
    
    // Inner class for passenger details
    public static class PassengerDetail {
        
        private String title;
        private String firstName;
        private String lastName;
        
        @JsonFormat(pattern = "yyyy-MM-dd")
        private LocalDate dateOfBirth;
        private String nationality;
        private String passportNumber;
        private String mealPreference;
        private String specialAssistance;
        
        // Constructors
        public PassengerDetail() {}
        
        public PassengerDetail(String title, String firstName, String lastName, LocalDate dateOfBirth) {
            this.title = title;
            this.firstName = firstName;
            this.lastName = lastName;
            this.dateOfBirth = dateOfBirth;
        }
        
        // Getters and Setters
        public String getTitle() {
            return title;
        }
        
        public void setTitle(String title) {
            this.title = title;
        }
        
        public String getFirstName() {
            return firstName;
        }
        
        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }
        
        public String getLastName() {
            return lastName;
        }
        
        public void setLastName(String lastName) {
            this.lastName = lastName;
        }
        
        public LocalDate getDateOfBirth() {
            return dateOfBirth;
        }
        
        public void setDateOfBirth(LocalDate dateOfBirth) {
            this.dateOfBirth = dateOfBirth;
        }
        
        public String getNationality() {
            return nationality;
        }
        
        public void setNationality(String nationality) {
            this.nationality = nationality;
        }
        
        public String getPassportNumber() {
            return passportNumber;
        }
        
        public void setPassportNumber(String passportNumber) {
            this.passportNumber = passportNumber;
        }
        
        public String getMealPreference() {
            return mealPreference;
        }
        
        public void setMealPreference(String mealPreference) {
            this.mealPreference = mealPreference;
        }
        
        public String getSpecialAssistance() {
            return specialAssistance;
        }
        
        public void setSpecialAssistance(String specialAssistance) {
            this.specialAssistance = specialAssistance;
        }
    }
}
