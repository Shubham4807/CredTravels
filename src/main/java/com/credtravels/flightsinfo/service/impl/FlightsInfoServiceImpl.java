package com.credtravels.flightsinfo.service.impl;

import com.credtravels.flightsinfo.model.Airline;
import com.credtravels.flightsinfo.model.Airport;
import com.credtravels.flightsinfo.model.FlightInfo;
import com.credtravels.flightsinfo.service.FlightsInfoService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class FlightsInfoServiceImpl implements FlightsInfoService {
    
    @Override
    public Optional<FlightInfo> getFlightInfo(Long flightId) {
        // TODO: Implement actual database query
        return Optional.empty();
    }
    
    @Override
    public List<FlightInfo> searchFlights(String airline, String route) {
        // TODO: Implement actual database query
        return new ArrayList<>();
    }
    
    @Override
    public List<Airline> getAllAirlines() {
        // TODO: Implement actual database query
        return new ArrayList<>();
    }
    
    @Override
    public Optional<Airline> getAirline(Long airlineId) {
        // TODO: Implement actual database query
        return Optional.empty();
    }
    
    @Override
    public List<Airport> getAllAirports() {
        // TODO: Implement actual database query
        return new ArrayList<>();
    }
    
    @Override
    public Optional<Airport> getAirport(Long airportId) {
        // TODO: Implement actual database query
        return Optional.empty();
    }
    
    @Override
    public List<Object> getRoutes() {
        // TODO: Implement actual database query
        return new ArrayList<>();
    }
    
    @Override
    public Optional<Object> getRoute(Long departureAirportId, Long arrivalAirportId) {
        // TODO: Implement actual database query
        return Optional.empty();
    }
}
