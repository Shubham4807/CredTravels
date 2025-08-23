package com.credtravels.inventory.repository;

import com.credtravels.inventory.model.FlightInventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface FlightInventoryRepository extends JpaRepository<FlightInventory, Long> {
    
    Optional<FlightInventory> findByFlightInfoIdAndFlightDate(Long flightInfoId, LocalDate flightDate);
    
    List<FlightInventory> findByFlightDate(LocalDate flightDate);
    
    @Query("SELECT fi FROM FlightInventory fi WHERE fi.flightDate = :date AND fi.status = 'ACTIVE'")
    List<FlightInventory> findActiveFlightsByDate(@Param("date") LocalDate date);
    
    @Query("SELECT fi FROM FlightInventory fi WHERE fi.flightInfoId = :flightInfoId AND fi.flightDate >= :fromDate AND fi.flightDate <= :toDate")
    List<FlightInventory> findByFlightInfoIdAndDateRange(@Param("flightInfoId") Long flightInfoId, 
                                                       @Param("fromDate") LocalDate fromDate, 
                                                       @Param("toDate") LocalDate toDate);
}
