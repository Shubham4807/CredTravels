package com.credtravels.flightsinfo.controller;

import com.credtravels.common.dto.ApiResponse;
import com.credtravels.flightsinfo.model.Airline;
import com.credtravels.flightsinfo.model.Airport;
import com.credtravels.flightsinfo.model.FlightInfo;
import com.credtravels.flightsinfo.service.FlightsInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/flights-info")
public class FlightsInfoController {
    
    @Autowired
    private FlightsInfoService flightsInfoService;
    
    /**
     * Get flight information by ID
     */
    @GetMapping("/flights/{flightId}")
    public ResponseEntity<ApiResponse<FlightInfo>> getFlightInfo(
            @PathVariable Long flightId) {
        
        Optional<FlightInfo> flightInfo = flightsInfoService.getFlightInfo(flightId);
        if (flightInfo.isPresent()) {
            return ResponseEntity.ok(ApiResponse.success(flightInfo.get()));
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    /**
     * Search flights by criteria
     */
    @GetMapping("/flights/search")
    public ResponseEntity<ApiResponse<List<FlightInfo>>> searchFlights(
            @RequestParam(required = false) String airline,
            @RequestParam(required = false) String route) {
        
        List<FlightInfo> flights = flightsInfoService.searchFlights(airline, route);
        return ResponseEntity.ok(ApiResponse.success(flights));
    }
    
    /**
     * Get all airlines
     */
    @GetMapping("/airlines")
    public ResponseEntity<ApiResponse<List<Airline>>> getAirlines() {
        
        List<Airline> airlines = flightsInfoService.getAllAirlines();
        return ResponseEntity.ok(ApiResponse.success(airlines));
    }
    
    /**
     * Get airline by ID
     */
    @GetMapping("/airlines/{airlineId}")
    public ResponseEntity<ApiResponse<Airline>> getAirline(
            @PathVariable Long airlineId) {
        
        Optional<Airline> airline = flightsInfoService.getAirline(airlineId);
        if (airline.isPresent()) {
            return ResponseEntity.ok(ApiResponse.success(airline.get()));
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    /**
     * Get all airports
     */
    @GetMapping("/airports")
    public ResponseEntity<ApiResponse<List<Airport>>> getAirports() {
        
        List<Airport> airports = flightsInfoService.getAllAirports();
        return ResponseEntity.ok(ApiResponse.success(airports));
    }
    
    /**
     * Get airport by ID
     */
    @GetMapping("/airports/{airportId}")
    public ResponseEntity<ApiResponse<Airport>> getAirport(
            @PathVariable Long airportId) {
        
        Optional<Airport> airport = flightsInfoService.getAirport(airportId);
        if (airport.isPresent()) {
            return ResponseEntity.ok(ApiResponse.success(airport.get()));
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    /**
     * Get routes information
     */
    @GetMapping("/routes")
    public ResponseEntity<ApiResponse<List<Object>>> getRoutes() {
        
        List<Object> routes = flightsInfoService.getRoutes();
        return ResponseEntity.ok(ApiResponse.success(routes));
    }
    
    /**
     * Get route by departure and arrival airports
     */
    @GetMapping("/routes/{departureAirportId}/{arrivalAirportId}")
    public ResponseEntity<ApiResponse<Object>> getRoute(
            @PathVariable Long departureAirportId,
            @PathVariable Long arrivalAirportId) {
        
        Optional<Object> route = flightsInfoService.getRoute(departureAirportId, arrivalAirportId);
        if (route.isPresent()) {
            return ResponseEntity.ok(ApiResponse.success(route.get()));
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
