package com.credtravels.inventory.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "seat_reservations")
public class SeatReservation {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "flight_inventory_id")
    private Long flightInventoryId;
    
    @Column(name = "reservation_id", unique = true, nullable = false)
    private String reservationId;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "seat_class")
    private SeatClass seatClass;
    
    @Column(name = "seat_count", nullable = false)
    private Integer seatCount;
    
    @Column(name = "reserved_until", nullable = false)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime reservedUntil;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private ReservationStatus status;
    
    @Column(name = "created_at")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
    
    // Constructors
    public SeatReservation() {
        this.createdAt = LocalDateTime.now();
        this.status = ReservationStatus.RESERVED;
    }
    
    public SeatReservation(Long flightInventoryId, String reservationId, SeatClass seatClass, 
                          Integer seatCount, LocalDateTime reservedUntil) {
        this();
        this.flightInventoryId = flightInventoryId;
        this.reservationId = reservationId;
        this.seatClass = seatClass;
        this.seatCount = seatCount;
        this.reservedUntil = reservedUntil;
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
    
    public String getReservationId() {
        return reservationId;
    }
    
    public void setReservationId(String reservationId) {
        this.reservationId = reservationId;
    }
    
    public SeatClass getSeatClass() {
        return seatClass;
    }
    
    public void setSeatClass(SeatClass seatClass) {
        this.seatClass = seatClass;
    }
    
    public Integer getSeatCount() {
        return seatCount;
    }
    
    public void setSeatCount(Integer seatCount) {
        this.seatCount = seatCount;
    }
    
    public LocalDateTime getReservedUntil() {
        return reservedUntil;
    }
    
    public void setReservedUntil(LocalDateTime reservedUntil) {
        this.reservedUntil = reservedUntil;
    }
    
    public ReservationStatus getStatus() {
        return status;
    }
    
    public void setStatus(ReservationStatus status) {
        this.status = status;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    // Business methods
    public boolean isExpired() {
        return LocalDateTime.now().isAfter(reservedUntil);
    }
    
    public boolean canBeConfirmed() {
        return status == ReservationStatus.RESERVED && !isExpired();
    }
    
    public enum SeatClass {
        ECONOMY, BUSINESS, FIRST
    }
    
    public enum ReservationStatus {
        RESERVED, CONFIRMED, EXPIRED
    }
}
