package com.credtravels.inventory.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "inventory_update_log")
public class InventoryUpdateLog {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "flight_inventory_id")
    private Long flightInventoryId;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "update_type", nullable = false)
    private UpdateType updateType;
    
    @Column(name = "old_values", columnDefinition = "JSON")
    private String oldValues;
    
    @Column(name = "new_values", columnDefinition = "JSON")
    private String newValues;
    
    @Column(name = "updated_by")
    private String updatedBy;
    
    @Column(name = "update_reason")
    private String updateReason;
    
    @Column(name = "created_at")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
    
    // Constructors
    public InventoryUpdateLog() {
        this.createdAt = LocalDateTime.now();
    }
    
    public InventoryUpdateLog(Long flightInventoryId, UpdateType updateType, String oldValues, 
                            String newValues, String updatedBy, String updateReason) {
        this();
        this.flightInventoryId = flightInventoryId;
        this.updateType = updateType;
        this.oldValues = oldValues;
        this.newValues = newValues;
        this.updatedBy = updatedBy;
        this.updateReason = updateReason;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Long getFlightInventoryId() {
        return flightInventoryId;
    }
    
    public void setFlightInventoryId(Long flightInventoryId) {
        this.flightInventoryId = flightInventoryId;
    }
    
    public UpdateType getUpdateType() {
        return updateType;
    }
    
    public void setUpdateType(UpdateType updateType) {
        this.updateType = updateType;
    }
    
    public String getOldValues() {
        return oldValues;
    }
    
    public void setOldValues(String oldValues) {
        this.oldValues = oldValues;
    }
    
    public String getNewValues() {
        return newValues;
    }
    
    public void setNewValues(String newValues) {
        this.newValues = newValues;
    }
    
    public String getUpdatedBy() {
        return updatedBy;
    }
    
    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }
    
    public String getUpdateReason() {
        return updateReason;
    }
    
    public void setUpdateReason(String updateReason) {
        this.updateReason = updateReason;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public enum UpdateType {
        MANUAL, BOOKING, CANCELLATION, EVENT
    }
}
