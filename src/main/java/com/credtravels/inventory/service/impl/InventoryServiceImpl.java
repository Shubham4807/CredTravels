package com.credtravels.inventory.service.impl;

import com.credtravels.common.exception.BusinessException;
import com.credtravels.common.exception.OptimisticLockException;
import com.credtravels.common.exception.ResourceNotFoundException;
import com.credtravels.inventory.dto.InventoryUpdateRequest;
import com.credtravels.inventory.dto.SeatReservationRequest;
import com.credtravels.inventory.model.FlightInventory;
import com.credtravels.inventory.model.InventoryUpdateLog;
import com.credtravels.inventory.model.SeatReservation;
import com.credtravels.inventory.repository.FlightInventoryRepository;
import com.credtravels.inventory.repository.InventoryUpdateLogRepository;
import com.credtravels.inventory.repository.SeatReservationRepository;
import com.credtravels.inventory.service.InventoryService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class InventoryServiceImpl implements InventoryService {
    
    private static final Logger log = LoggerFactory.getLogger(InventoryServiceImpl.class);
    
    @Autowired
    private FlightInventoryRepository flightInventoryRepository;
    
    @Autowired
    private SeatReservationRepository seatReservationRepository;
    
    @Autowired
    private InventoryUpdateLogRepository inventoryUpdateLogRepository;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    @Override
    public Optional<FlightInventory> getFlightInventory(Long flightId, LocalDate flightDate) {
        log.debug("Getting flight inventory for flightId: {} and date: {}", flightId, flightDate);
        return flightInventoryRepository.findByFlightInfoIdAndFlightDate(flightId, flightDate);
    }
    
    @Override
    @CacheEvict(value = "inventory", key = "#flightId + '_' + #flightDate")
    public FlightInventory updateInventory(Long flightId, LocalDate flightDate, InventoryUpdateRequest request) {
        log.info("Updating inventory for flightId: {} and date: {}", flightId, flightDate);
        
        Optional<FlightInventory> existingInventory = getFlightInventory(flightId, flightDate);
        FlightInventory inventory;
        
        if (existingInventory.isPresent()) {
            inventory = existingInventory.get();
            // Log the update
            logInventoryUpdate(inventory, request, "MANUAL", request.getUpdatedBy());
        } else {
            inventory = new FlightInventory();
            inventory.setFlightInfoId(flightId);
            inventory.setFlightDate(flightDate);
        }
        
        try {
            inventory.setAvailableSeats(objectMapper.writeValueAsString(request.getAvailableSeats()));
            inventory.setPricing(objectMapper.writeValueAsString(request.getPricing()));
            inventory.setTotalCapacity(objectMapper.writeValueAsString(request.getTotalCapacity()));
            inventory.updateLastUpdated();
        } catch (JsonProcessingException e) {
            throw new BusinessException("Failed to serialize inventory data: " + e.getMessage());
        }
        
        FlightInventory savedInventory = flightInventoryRepository.save(inventory);
        log.info("Inventory updated successfully for flightId: {} and date: {}", flightId, flightDate);
        
        return savedInventory;
    }
    
    @Override
    public FlightInventory getFlightAvailability(Long flightId, LocalDate flightDate) {
        log.debug("Getting flight availability for flightId: {} and date: {}", flightId, flightDate);
        
        return flightInventoryRepository.findByFlightInfoIdAndFlightDate(flightId, flightDate)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Flight inventory not found for flightId: " + flightId + " and date: " + flightDate));
    }
    
    @Override
    @Transactional
    public SeatReservation reserveSeats(Long flightId, LocalDate flightDate, SeatReservationRequest request) {
        log.info("Reserving {} seats of class {} for flightId: {} and date: {}", 
                request.getSeatCount(), request.getSeatClass(), flightId, flightDate);
        
        FlightInventory inventory = getFlightAvailability(flightId, flightDate);
        
        // Validate seat availability
        validateSeatAvailability(inventory, request.getSeatClass(), request.getSeatCount());
        
        // Create seat reservation
        String reservationId = generateReservationId();
        LocalDateTime reservedUntil = LocalDateTime.now().plusSeconds(900); // 15 minutes
        
        SeatReservation reservation = new SeatReservation(
                inventory.getId(), reservationId, request.getSeatClass(), 
                request.getSeatCount(), reservedUntil);
        
        SeatReservation savedReservation = seatReservationRepository.save(reservation);
        
        // Update inventory
        updateInventoryForReservation(inventory, request.getSeatClass(), request.getSeatCount());
        
        log.info("Seats reserved successfully with reservationId: {}", reservationId);
        return savedReservation;
    }
    
    @Override
    @Transactional
    public void releaseSeatReservation(String reservationId) {
        log.info("Releasing seat reservation: {}", reservationId);
        
        SeatReservation reservation = seatReservationRepository.findByReservationId(reservationId)
                .orElseThrow(() -> new ResourceNotFoundException("Reservation not found: " + reservationId));
        
        if (reservation.getStatus() == SeatReservation.ReservationStatus.CONFIRMED) {
            throw new BusinessException("Cannot release confirmed reservation: " + reservationId);
        }
        
        // Update inventory back
        FlightInventory inventory = flightInventoryRepository.findById(reservation.getFlightInventoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Flight inventory not found"));
        
        updateInventoryForRelease(inventory, reservation.getSeatClass(), reservation.getSeatCount());
        
        // Delete reservation
        seatReservationRepository.delete(reservation);
        
        log.info("Seat reservation released successfully: {}", reservationId);
    }
    
    @Override
    @Transactional
    public SeatReservation confirmSeatReservation(String reservationId) {
        log.info("Confirming seat reservation: {}", reservationId);
        
        SeatReservation reservation = seatReservationRepository.findByReservationId(reservationId)
                .orElseThrow(() -> new ResourceNotFoundException("Reservation not found: " + reservationId));
        
        if (!reservation.canBeConfirmed()) {
            throw new BusinessException("Reservation cannot be confirmed: " + reservationId);
        }
        
        reservation.setStatus(SeatReservation.ReservationStatus.CONFIRMED);
        SeatReservation confirmedReservation = seatReservationRepository.save(reservation);
        
        log.info("Seat reservation confirmed successfully: {}", reservationId);
        return confirmedReservation;
    }
    
    @Override
    public List<FlightInventory> searchFlightsByDateAndRoute(LocalDate date, String departureAirport, String arrivalAirport) {
        log.debug("Searching flights by date: {} from: {} to: {}", date, departureAirport, arrivalAirport);
        
        // This would typically involve joining with flight_info table
        // For now, returning all flights for the given date
        return flightInventoryRepository.findByFlightDate(date);
    }
    
    @Override
    @Transactional
    public List<FlightInventory> batchUpdateInventory(List<InventoryUpdateRequest> requests) {
        log.info("Performing batch update for {} inventory items", requests.size());
        
        List<FlightInventory> updatedInventories = requests.stream()
                .map(request -> updateInventory(
                        request.getFlightInfoId(), 
                        request.getFlightDate(), 
                        request))
                .toList();
        
        log.info("Batch update completed successfully for {} items", updatedInventories.size());
        return updatedInventories;
    }
    
    @Override
    public List<SeatReservation> getExpiredReservations() {
        log.debug("Getting expired reservations");
        return seatReservationRepository.findExpiredReservations(LocalDateTime.now());
    }
    
    @Override
    @Scheduled(fixedRate = 300000) // Every 5 minutes
    public void cleanupExpiredReservations() {
        log.info("Starting cleanup of expired reservations");
        
        List<SeatReservation> expiredReservations = getExpiredReservations();
        
        for (SeatReservation reservation : expiredReservations) {
            try {
                releaseSeatReservation(reservation.getReservationId());
            } catch (Exception e) {
                log.error("Failed to cleanup expired reservation: {}", reservation.getReservationId(), e);
            }
        }
        
        log.info("Cleanup completed. Processed {} expired reservations", expiredReservations.size());
    }
    
    // Private helper methods
    
    private void validateSeatAvailability(FlightInventory inventory, SeatReservation.SeatClass seatClass, Integer seatCount) {
        try {
            String availableSeatsJson = inventory.getAvailableSeats();
            // Parse JSON and check availability
            // This is a simplified validation
            if (seatCount > 9) {
                throw new BusinessException("Maximum 9 seats can be reserved at once");
            }
        } catch (Exception e) {
            throw new BusinessException("Failed to validate seat availability: " + e.getMessage());
        }
    }
    
    private void updateInventoryForReservation(FlightInventory inventory, SeatReservation.SeatClass seatClass, Integer seatCount) {
        // Update available seats in inventory
        // This is a simplified implementation
        inventory.updateLastUpdated();
        flightInventoryRepository.save(inventory);
    }
    
    private void updateInventoryForRelease(FlightInventory inventory, SeatReservation.SeatClass seatClass, Integer seatCount) {
        // Update available seats back in inventory
        // This is a simplified implementation
        inventory.updateLastUpdated();
        flightInventoryRepository.save(inventory);
    }
    
    private String generateReservationId() {
        return "RES" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
    
    private void logInventoryUpdate(FlightInventory inventory, InventoryUpdateRequest request, String updateType, String updatedBy) {
        try {
            String oldValues = objectMapper.writeValueAsString(inventory);
            String newValues = objectMapper.writeValueAsString(request);
            
            InventoryUpdateLog log = new InventoryUpdateLog(
                    inventory.getId(), 
                    InventoryUpdateLog.UpdateType.valueOf(updateType),
                    oldValues, 
                    newValues, 
                    updatedBy, 
                    request.getUpdateReason());
            
            inventoryUpdateLogRepository.save(log);
        } catch (JsonProcessingException e) {
            log.error("Failed to log inventory update", e);
        }
    }
}
