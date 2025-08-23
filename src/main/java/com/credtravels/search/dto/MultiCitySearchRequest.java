package com.credtravels.search.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.List;

public class MultiCitySearchRequest {
    
    @NotEmpty(message = "At least one segment is required")
    @Size(max = 5, message = "Maximum 5 segments allowed")
    @Valid
    private List<FlightSegment> segments;
    
    private String seatClass;
    private Integer passengers = 1;
    
    // Constructors
    public MultiCitySearchRequest() {}
    
    public MultiCitySearchRequest(List<FlightSegment> segments) {
        this.segments = segments;
    }
    
    // Getters and Setters
    public List<FlightSegment> getSegments() {
        return segments;
    }
    
    public void setSegments(List<FlightSegment> segments) {
        this.segments = segments;
    }
    
    public String getSeatClass() {
        return seatClass;
    }
    
    public void setSeatClass(String seatClass) {
        this.seatClass = seatClass;
    }
    
    public Integer getPassengers() {
        return passengers;
    }
    
    public void setPassengers(Integer passengers) {
        this.passengers = passengers;
    }
    
    // Inner class for flight segments
    public static class FlightSegment {
        
        private String from;
        private String to;
        
        @JsonFormat(pattern = "yyyy-MM-dd")
        private LocalDate date;
        
        // Constructors
        public FlightSegment() {}
        
        public FlightSegment(String from, String to, LocalDate date) {
            this.from = from;
            this.to = to;
            this.date = date;
        }
        
        // Getters and Setters
        public String getFrom() {
            return from;
        }
        
        public void setFrom(String from) {
            this.from = from;
        }
        
        public String getTo() {
            return to;
        }
        
        public void setTo(String to) {
            this.to = to;
        }
        
        public LocalDate getDate() {
            return date;
        }
        
        public void setDate(LocalDate date) {
            this.date = date;
        }
    }
}
