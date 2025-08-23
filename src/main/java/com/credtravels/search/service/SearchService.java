package com.credtravels.search.service;

import com.credtravels.search.dto.FlightSearchRequest;
import com.credtravels.search.dto.FlightSearchResponse;
import com.credtravels.search.dto.MultiCitySearchRequest;
import com.credtravels.search.model.SearchFlight;

import java.time.LocalDate;
import java.util.List;

public interface SearchService {
    
    /**
     * Search flights with multi-hop support
     */
    FlightSearchResponse searchFlights(FlightSearchRequest request);
    
    /**
     * Search direct flights only
     */
    List<SearchFlight> searchDirectFlights(String from, String to, LocalDate date);
    
    /**
     * Search airports by query
     */
    List<String> searchAirports(String query);
    
    /**
     * Get airport suggestions
     */
    List<String> getAirportSuggestions(String query);
    
    /**
     * Multi-city search
     */
    FlightSearchResponse searchMultiCity(MultiCitySearchRequest request);
    
    /**
     * Get popular routes
     */
    List<String> getPopularRoutes();
    
    /**
     * Build and maintain search index
     */
    void buildSearchIndex();
    
    /**
     * Update search index for a specific flight
     */
    void updateSearchIndex(Long flightInfoId);
}
