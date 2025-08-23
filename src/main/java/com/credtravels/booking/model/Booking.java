package com.credtravels.booking.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "booking")
public class Booking {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "booking_reference", unique = true, nullable = false, length = 10)
    private String bookingReference;
    
    @Column(name = "customer_id", nullable = false)
    private Long customerId;
    
    @Column(name = "flight_info_id", nullable = false)
    private Long flightInfoId;
    
    @Column(name = "flight_date", nullable = false)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate flightDate;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "seat_class", nullable = false)
    private SeatClass seatClass;
    
    @Column(name = "passenger_count", nullable = false)
    private Integer passengerCount;
    
    @Column(name = "total_amount", nullable = false, precision = 12, scale = 2)
    private BigDecimal totalAmount;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "payment_status")
    private PaymentStatus paymentStatus;
    
    @Column(name = "payment_reference", length = 100)
    private String paymentReference;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "booking_status")
    private BookingStatus bookingStatus;
    

    
    @Column(name = "confirmation_date")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime confirmationDate;
    
    @Column(name = "cancellation_date")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime cancellationDate;
    
    @Version
    private Long version;
    
    @Column(name = "special_requests", columnDefinition = "TEXT")
    private String specialRequests;
    
    @Column(name = "created_at")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;
    
    // Constructors
    public Booking() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();

        this.paymentStatus = PaymentStatus.PENDING;
        this.bookingStatus = BookingStatus.RESERVED;
        this.version = 1L;
    }
    
    public Booking(String bookingReference, Long customerId, Long flightInfoId, 
                  LocalDate flightDate, SeatClass seatClass, Integer passengerCount, 
                  BigDecimal totalAmount) {
        this();
        this.bookingReference = bookingReference;
        this.customerId = customerId;
        this.flightInfoId = flightInfoId;
        this.flightDate = flightDate;
        this.seatClass = seatClass;
        this.passengerCount = passengerCount;
        this.totalAmount = totalAmount;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getBookingReference() {
        return bookingReference;
    }
    
    public void setBookingReference(String bookingReference) {
        this.bookingReference = bookingReference;
    }
    
    public Long getCustomerId() {
        return customerId;
    }
    
    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }
    
    public Long getFlightInfoId() {
        return flightInfoId;
    }
    
    public void setFlightInfoId(Long flightInfoId) {
        this.flightInfoId = flightInfoId;
    }
    
    public LocalDate getFlightDate() {
        return flightDate;
    }
    
    public void setFlightDate(LocalDate flightDate) {
        this.flightDate = flightDate;
    }
    
    public SeatClass getSeatClass() {
        return seatClass;
    }
    
    public void setSeatClass(SeatClass seatClass) {
        this.seatClass = seatClass;
    }
    
    public Integer getPassengerCount() {
        return passengerCount;
    }
    
    public void setPassengerCount(Integer passengerCount) {
        this.passengerCount = passengerCount;
    }
    
    public BigDecimal getTotalAmount() {
        return totalAmount;
    }
    
    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }
    
    public PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }
    
    public void setPaymentStatus(PaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
    }
    
    public String getPaymentReference() {
        return paymentReference;
    }
    
    public void setPaymentReference(String paymentReference) {
        this.paymentReference = paymentReference;
    }
    
    public BookingStatus getBookingStatus() {
        return bookingStatus;
    }
    
    public void setBookingStatus(BookingStatus bookingStatus) {
        this.bookingStatus = bookingStatus;
    }
    
    
    
    public LocalDateTime getConfirmationDate() {
        return confirmationDate;
    }
    
    public void setConfirmationDate(LocalDateTime confirmationDate) {
        this.confirmationDate = confirmationDate;
    }
    
    public LocalDateTime getCancellationDate() {
        return cancellationDate;
    }
    
    public void setCancellationDate(LocalDateTime cancellationDate) {
        this.cancellationDate = cancellationDate;
    }
    
    public Long getVersion() {
        return version;
    }
    
    public void setVersion(Long version) {
        this.version = version;
    }
    
    public String getSpecialRequests() {
        return specialRequests;
    }
    
    public void setSpecialRequests(String specialRequests) {
        this.specialRequests = specialRequests;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    // Business methods
    public void updateTimestamp() {
        this.updatedAt = LocalDateTime.now();
    }
    
    public void confirm() {
        this.bookingStatus = BookingStatus.CONFIRMED;
        this.confirmationDate = LocalDateTime.now();
        updateTimestamp();
    }
    
    public void cancel() {
        this.bookingStatus = BookingStatus.CANCELLED;
        this.cancellationDate = LocalDateTime.now();
        updateTimestamp();
    }
    
    public void completePayment(String paymentReference) {
        this.paymentStatus = PaymentStatus.COMPLETED;
        this.paymentReference = paymentReference;
        updateTimestamp();
    }
    
    public enum SeatClass {
        ECONOMY, BUSINESS, FIRST
    }
    
    public enum PaymentStatus {
        PENDING, COMPLETED, FAILED, REFUNDED
    }
    
    public enum BookingStatus {
        RESERVED, CONFIRMED, CANCELLED, COMPLETED
    }
}
