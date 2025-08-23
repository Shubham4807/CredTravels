package com.credtravels.inventory.controller;

import com.credtravels.common.dto.ApiResponse;
import com.credtravels.inventory.dto.InventoryUpdateRequest;
import com.credtravels.inventory.dto.SeatReservationRequest;
import com.credtravels.inventory.model.FlightInventory;
import com.credtravels.inventory.model.SeatReservation;
import com.credtravels.inventory.service.InventoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/inventory")
public class InventoryController {
    
    @Autowired
    private InventoryService inventoryService;
    
    /**
     * Update flight inventory
     */
    @PostMapping("/flights/{flightId}/update")
    public ResponseEntity<ApiResponse<FlightInventory>> updateInventory(
            @PathVariable Long flightId,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate flightDate,
            @Valid @RequestBody InventoryUpdateRequest request) {
        
        FlightInventory updatedInventory = inventoryService.updateInventory(flightId, flightDate, request);
        return ResponseEntity.ok(ApiResponse.success("Inventory updated successfully", updatedInventory));
    }
    
    /**
     * Get flight inventory
     */
    @GetMapping("/flights/{flightId}")
    public ResponseEntity<ApiResponse<FlightInventory>> getFlightInventory(
            @PathVariable Long flightId,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate flightDate) {
        
        return inventoryService.getFlightInventory(flightId, flightDate)
                .map(inventory -> ResponseEntity.ok(ApiResponse.success(inventory)))
                .orElse(ResponseEntity.notFound().build());
    }
    
    /**
     * Get flight availability
     */
    @GetMapping("/flights/{flightId}/availability")
    public ResponseEntity<ApiResponse<FlightInventory>> getFlightAvailability(
            @PathVariable Long flightId,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate flightDate) {
        
        FlightInventory availability = inventoryService.getFlightAvailability(flightId, flightDate);
        return ResponseEntity.ok(ApiResponse.success(availability));
    }
    
    /**
     * Reserve seats
     */
    @PutMapping("/flights/{flightId}/reserve")
    public ResponseEntity<ApiResponse<SeatReservation>> reserveSeats(
            @PathVariable Long flightId,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate flightDate,
            @Valid @RequestBody SeatReservationRequest request) {
        
        SeatReservation reservation = inventoryService.reserveSeats(flightId, flightDate, request);
        return ResponseEntity.ok(ApiResponse.success("Seats reserved successfully", reservation));
    }
    
    /**
     * Release seat reservation
     */
    @PutMapping("/flights/{flightId}/release")
    public ResponseEntity<ApiResponse<Void>> releaseSeatReservation(
            @PathVariable Long flightId,
            @RequestParam String reservationId) {
        
        inventoryService.releaseSeatReservation(reservationId);
        return ResponseEntity.ok(ApiResponse.success("Seat reservation released successfully", null));
    }
    
    /**
     * Search flights by date and route
     */
    @GetMapping("/flights/search")
    public ResponseEntity<ApiResponse<List<FlightInventory>>> searchFlights(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date,
            @RequestParam String from,
            @RequestParam String to) {
        
        List<FlightInventory> flights = inventoryService.searchFlightsByDateAndRoute(date, from, to);
        return ResponseEntity.ok(ApiResponse.success(flights));
    }
    
    /**
     * Batch update inventory
     */
    @PostMapping("/batch-update")
    public ResponseEntity<ApiResponse<List<FlightInventory>>> batchUpdateInventory(
            @Valid @RequestBody List<InventoryUpdateRequest> requests) {
        
        List<FlightInventory> updatedInventories = inventoryService.batchUpdateInventory(requests);
        return ResponseEntity.ok(ApiResponse.success("Batch update completed successfully", updatedInventories));
    }
    
    /**
     * Get expired reservations
     */
    @GetMapping("/reservations/expired")
    public ResponseEntity<ApiResponse<List<SeatReservation>>> getExpiredReservations() {
        
        List<SeatReservation> expiredReservations = inventoryService.getExpiredReservations();
        return ResponseEntity.ok(ApiResponse.success(expiredReservations));
    }
    
    /**
     * Clean up expired reservations
     */
    @PostMapping("/reservations/cleanup")
    public ResponseEntity<ApiResponse<Void>> cleanupExpiredReservations() {
        
        inventoryService.cleanupExpiredReservations();
        return ResponseEntity.ok(ApiResponse.success("Cleanup completed successfully", null));
    }
}
