package com.credtravels.inventory.repository;

import com.credtravels.inventory.model.SeatReservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface SeatReservationRepository extends JpaRepository<SeatReservation, Long> {
    
    Optional<SeatReservation> findByReservationId(String reservationId);
    
    List<SeatReservation> findByFlightInventoryId(Long flightInventoryId);
    
    @Query("SELECT sr FROM SeatReservation sr WHERE sr.reservedUntil < :now AND sr.status = 'RESERVED'")
    List<SeatReservation> findExpiredReservations(@Param("now") LocalDateTime now);
    
    @Query("SELECT sr FROM SeatReservation sr WHERE sr.flightInventoryId = :flightInventoryId AND sr.status = 'RESERVED'")
    List<SeatReservation> findActiveReservationsByFlight(@Param("flightInventoryId") Long flightInventoryId);
    
    @Query("SELECT sr FROM SeatReservation sr WHERE sr.reservedUntil < :now")
    List<SeatReservation> findExpiredReservations();
}
