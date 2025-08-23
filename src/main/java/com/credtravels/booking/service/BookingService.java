package com.credtravels.booking.service;

import com.credtravels.booking.dto.BookingRequest;
import com.credtravels.booking.dto.BookingConfirmationRequest;
import com.credtravels.booking.dto.BookingModificationRequest;
import com.credtravels.booking.model.Booking;

import java.util.List;
import java.util.Optional;

public interface BookingService {
    
    /**
     * Reserve a flight
     */
    Booking reserveFlight(BookingRequest request);
    
    /**
     * Confirm a booking with payment
     */
    Booking confirmBooking(BookingConfirmationRequest request);
    
    /**
     * Get booking details
     */
    Optional<Booking> getBooking(String bookingReference);
    
    /**
     * Modify a booking
     */
    Booking modifyBooking(String bookingReference, BookingModificationRequest request);
    
    /**
     * Cancel a booking
     */
    Booking cancelBooking(String bookingReference);
    
    /**
     * Get user bookings
     */
    List<Booking> getUserBookings(String userId);
    
    /**
     * Process payment callback
     */
    void processPaymentCallback(String bookingReference, String transactionId, String status);
    
    /**
     * Generate booking reference
     */
    String generateBookingReference();
    
    /**
     * Validate booking request
     */
    void validateBookingRequest(BookingRequest request);
}
