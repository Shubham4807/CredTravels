package com.credtravels.booking.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.Map;

public class BookingConfirmationRequest {
    
    @NotBlank(message = "Booking reference is required")
    private String bookingReference;
    
    @NotNull(message = "Payment method is required")
    private String paymentMethod;
    
    @NotNull(message = "Payment details are required")
    private Map<String, Object> paymentDetails;
    
    private String customerNotes;
    
    // Constructors
    public BookingConfirmationRequest() {}
    
    public BookingConfirmationRequest(String bookingReference, String paymentMethod, Map<String, Object> paymentDetails) {
        this.bookingReference = bookingReference;
        this.paymentMethod = paymentMethod;
        this.paymentDetails = paymentDetails;
    }
    
    // Getters and Setters
    public String getBookingReference() {
        return bookingReference;
    }
    
    public void setBookingReference(String bookingReference) {
        this.bookingReference = bookingReference;
    }
    
    public String getPaymentMethod() {
        return paymentMethod;
    }
    
    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
    
    public Map<String, Object> getPaymentDetails() {
        return paymentDetails;
    }
    
    public void setPaymentDetails(Map<String, Object> paymentDetails) {
        this.paymentDetails = paymentDetails;
    }
    
    public String getCustomerNotes() {
        return customerNotes;
    }
    
    public void setCustomerNotes(String customerNotes) {
        this.customerNotes = customerNotes;
    }
}
