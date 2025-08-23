package com.credtravels.inventory.repository;

import com.credtravels.inventory.model.InventoryUpdateLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface InventoryUpdateLogRepository extends JpaRepository<InventoryUpdateLog, Long> {
    
    List<InventoryUpdateLog> findByFlightInventoryIdOrderByCreatedAtDesc(Long flightInventoryId);
    
    @Query("SELECT iul FROM InventoryUpdateLog iul WHERE iul.createdAt >= :fromDate AND iul.createdAt <= :toDate")
    List<InventoryUpdateLog> findByDateRange(@Param("fromDate") LocalDateTime fromDate, 
                                           @Param("toDate") LocalDateTime toDate);
    
    @Query("SELECT iul FROM InventoryUpdateLog iul WHERE iul.updateType = :updateType")
    List<InventoryUpdateLog> findByUpdateType(@Param("updateType") InventoryUpdateLog.UpdateType updateType);
}
