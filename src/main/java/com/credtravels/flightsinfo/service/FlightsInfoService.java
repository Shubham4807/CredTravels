package com.credtravels.flightsinfo.service;

import com.credtravels.flightsinfo.model.Airline;
import com.credtravels.flightsinfo.model.Airport;
import com.credtravels.flightsinfo.model.FlightInfo;

import java.util.List;
import java.util.Optional;

public interface FlightsInfoService {
    
    /**
     * Get flight information by ID
     */
    Optional<FlightInfo> getFlightInfo(Long flightId);
    
    /**
     * Search flights by criteria
     */
    List<FlightInfo> searchFlights(String airline, String route);
    
    /**
     * Get all airlines
     */
    List<Airline> getAllAirlines();
    
    /**
     * Get airline by ID
     */
    Optional<Airline> getAirline(Long airlineId);
    
    /**
     * Get all airports
     */
    List<Airport> getAllAirports();
    
    /**
     * Get airport by ID
     */
    Optional<Airport> getAirport(Long airportId);
    
    /**
     * Get routes information
     */
    List<Object> getRoutes();
    
    /**
     * Get route by departure and arrival airports
     */
    Optional<Object> getRoute(Long departureAirportId, Long arrivalAirportId);
}
