package com.credtravels.search.controller;

import com.credtravels.common.dto.ApiResponse;
import com.credtravels.search.dto.FlightSearchRequest;
import com.credtravels.search.dto.FlightSearchResponse;
import com.credtravels.search.dto.MultiCitySearchRequest;
import com.credtravels.search.model.SearchFlight;
import com.credtravels.search.service.SearchService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/search")
public class SearchController {
    
    @Autowired
    private SearchService searchService;
    
    /**
     * Search flights with multi-hop support
     */
    @GetMapping("/flights")
    public ResponseEntity<ApiResponse<FlightSearchResponse>> searchFlights(
            @RequestParam String from,
            @RequestParam String to,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date,
            @RequestParam(defaultValue = "1") Integer maxHops,
            @RequestParam(defaultValue = "50") Integer limit) {
        
        FlightSearchRequest request = new FlightSearchRequest();
        request.setFrom(from);
        request.setTo(to);
        request.setDate(date);
        request.setMaxHops(maxHops);
        request.setLimit(limit);
        
        FlightSearchResponse response = searchService.searchFlights(request);
        return ResponseEntity.ok(ApiResponse.success(response));
    }
    
    /**
     * Search direct flights only
     */
    @GetMapping("/flights/direct")
    public ResponseEntity<ApiResponse<List<SearchFlight>>> searchDirectFlights(
            @RequestParam String from,
            @RequestParam String to,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date) {
        
        List<SearchFlight> directFlights = searchService.searchDirectFlights(from, to, date);
        return ResponseEntity.ok(ApiResponse.success(directFlights));
    }
    
    /**
     * Search airports by query
     */
    @GetMapping("/airports")
    public ResponseEntity<ApiResponse<List<String>>> searchAirports(
            @RequestParam String query) {
        
        List<String> airports = searchService.searchAirports(query);
        return ResponseEntity.ok(ApiResponse.success(airports));
    }
    
    /**
     * Get airport suggestions
     */
    @GetMapping("/suggestions")
    public ResponseEntity<ApiResponse<List<String>>> getAirportSuggestions(
            @RequestParam String query) {
        
        List<String> suggestions = searchService.getAirportSuggestions(query);
        return ResponseEntity.ok(ApiResponse.success(suggestions));
    }
    
    /**
     * Multi-city search
     */
    @PostMapping("/flights/multi-city")
    public ResponseEntity<ApiResponse<FlightSearchResponse>> searchMultiCity(
            @Valid @RequestBody MultiCitySearchRequest request) {
        
        FlightSearchResponse response = searchService.searchMultiCity(request);
        return ResponseEntity.ok(ApiResponse.success(response));
    }
    
    /**
     * Get popular routes
     */
    @GetMapping("/popular-routes")
    public ResponseEntity<ApiResponse<List<String>>> getPopularRoutes() {
        
        List<String> popularRoutes = searchService.getPopularRoutes();
        return ResponseEntity.ok(ApiResponse.success(popularRoutes));
    }
    
    /**
     * Build search index (admin endpoint)
     */
    @PostMapping("/index/build")
    public ResponseEntity<ApiResponse<Void>> buildSearchIndex() {
        
        searchService.buildSearchIndex();
        return ResponseEntity.ok(ApiResponse.success("Search index built successfully", null));
    }
    
    /**
     * Update search index for specific flight (admin endpoint)
     */
    @PostMapping("/index/update/{flightInfoId}")
    public ResponseEntity<ApiResponse<Void>> updateSearchIndex(
            @PathVariable Long flightInfoId) {
        
        searchService.updateSearchIndex(flightInfoId);
        return ResponseEntity.ok(ApiResponse.success("Search index updated successfully", null));
    }
}
