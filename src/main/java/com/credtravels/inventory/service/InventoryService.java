package com.credtravels.inventory.service;

import com.credtravels.inventory.dto.InventoryUpdateRequest;
import com.credtravels.inventory.dto.SeatReservationRequest;
import com.credtravels.inventory.model.FlightInventory;
import com.credtravels.inventory.model.SeatReservation;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface InventoryService {
    
    /**
     * Get flight inventory by ID
     */
    Optional<FlightInventory> getFlightInventory(Long flightId, LocalDate flightDate);
    
    /**
     * Update flight inventory
     */
    FlightInventory updateInventory(Long flightId, LocalDate flightDate, InventoryUpdateRequest request);
    
    /**
     * Get flight availability
     */
    FlightInventory getFlightAvailability(Long flightId, LocalDate flightDate);
    
    /**
     * Reserve seats for a flight
     */
    SeatReservation reserveSeats(Long flightId, LocalDate flightDate, SeatReservationRequest request);
    
    /**
     * Release seat reservation
     */
    void releaseSeatReservation(String reservationId);
    
    /**
     * Confirm seat reservation
     */
    SeatReservation confirmSeatReservation(String reservationId);
    
    /**
     * Search flights by date and route
     */
    List<FlightInventory> searchFlightsByDateAndRoute(LocalDate date, String departureAirport, String arrivalAirport);
    
    /**
     * Batch update inventory
     */
    List<FlightInventory> batchUpdateInventory(List<InventoryUpdateRequest> requests);
    
    /**
     * Get expired reservations
     */
    List<SeatReservation> getExpiredReservations();
    
    /**
     * Clean up expired reservations
     */
    void cleanupExpiredReservations();
}
