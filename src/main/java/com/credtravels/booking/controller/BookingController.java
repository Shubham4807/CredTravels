package com.credtravels.booking.controller;

import com.credtravels.common.dto.ApiResponse;
import com.credtravels.booking.dto.BookingRequest;
import com.credtravels.booking.dto.BookingConfirmationRequest;
import com.credtravels.booking.dto.BookingModificationRequest;
import com.credtravels.booking.model.Booking;
import com.credtravels.booking.service.BookingService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/booking")
public class BookingController {
    
    @Autowired
    private BookingService bookingService;
    
    /**
     * Reserve a flight
     */
    @PostMapping("/flights/reserve")
    public ResponseEntity<ApiResponse<Booking>> reserveFlight(
            @Valid @RequestBody BookingRequest request) {
        
        bookingService.validateBookingRequest(request);
        Booking booking = bookingService.reserveFlight(request);
        return ResponseEntity.ok(ApiResponse.success("Flight reserved successfully", booking));
    }
    
    /**
     * Confirm a booking with payment
     */
    @PostMapping("/flights/confirm")
    public ResponseEntity<ApiResponse<Booking>> confirmBooking(
            @Valid @RequestBody BookingConfirmationRequest request) {
        
        Booking confirmedBooking = bookingService.confirmBooking(request);
        return ResponseEntity.ok(ApiResponse.success("Booking confirmed successfully", confirmedBooking));
    }
    
    /**
     * Get booking details
     */
    @GetMapping("/{bookingReference}")
    public ResponseEntity<ApiResponse<Booking>> getBooking(
            @PathVariable String bookingReference) {
        
        Optional<Booking> booking = bookingService.getBooking(bookingReference);
        if (booking.isPresent()) {
            return ResponseEntity.ok(ApiResponse.success(booking.get()));
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    /**
     * Modify a booking
     */
    @PutMapping("/{bookingReference}/modify")
    public ResponseEntity<ApiResponse<Booking>> modifyBooking(
            @PathVariable String bookingReference,
            @Valid @RequestBody BookingModificationRequest request) {
        
        Booking modifiedBooking = bookingService.modifyBooking(bookingReference, request);
        return ResponseEntity.ok(ApiResponse.success("Booking modified successfully", modifiedBooking));
    }
    
    /**
     * Cancel a booking
     */
    @DeleteMapping("/{bookingReference}/cancel")
    public ResponseEntity<ApiResponse<Booking>> cancelBooking(
            @PathVariable String bookingReference) {
        
        Booking cancelledBooking = bookingService.cancelBooking(bookingReference);
        return ResponseEntity.ok(ApiResponse.success("Booking cancelled successfully", cancelledBooking));
    }
    
    /**
     * Get user bookings
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<List<Booking>>> getUserBookings(
            @PathVariable String userId) {
        
        List<Booking> userBookings = bookingService.getUserBookings(userId);
        return ResponseEntity.ok(ApiResponse.success(userBookings));
    }
    
    /**
     * Process payment callback
     */
    @PostMapping("/payment/callback")
    public ResponseEntity<ApiResponse<Void>> processPaymentCallback(
            @RequestParam String bookingReference,
            @RequestParam String transactionId,
            @RequestParam String status) {
        
        bookingService.processPaymentCallback(bookingReference, transactionId, status);
        return ResponseEntity.ok(ApiResponse.success("Payment callback processed successfully", null));
    }
}
