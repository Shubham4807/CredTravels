package com.credtravels.search.dto;

import com.credtravels.search.model.SearchFlight;

import java.util.List;

public class FlightSearchResponse {
    
    private List<SearchFlight> directFlights;
    private List<SearchFlight> multiHopFlights;
    private Integer totalResults;
    private Long searchTimeMs;
    private String searchQuery;
    
    // Constructors
    public FlightSearchResponse() {}
    
    public FlightSearchResponse(List<SearchFlight> directFlights, List<SearchFlight> multiHopFlights) {
        this.directFlights = directFlights;
        this.multiHopFlights = multiHopFlights;
        this.totalResults = (directFlights != null ? directFlights.size() : 0) + 
                           (multiHopFlights != null ? multiHopFlights.size() : 0);
    }
    
    // Getters and Setters
    public List<SearchFlight> getDirectFlights() {
        return directFlights;
    }
    
    public void setDirectFlights(List<SearchFlight> directFlights) {
        this.directFlights = directFlights;
    }
    
    public List<SearchFlight> getMultiHopFlights() {
        return multiHopFlights;
    }
    
    public void setMultiHopFlights(List<SearchFlight> multiHopFlights) {
        this.multiHopFlights = multiHopFlights;
    }
    
    public Integer getTotalResults() {
        return totalResults;
    }
    
    public void setTotalResults(Integer totalResults) {
        this.totalResults = totalResults;
    }
    
    public Long getSearchTimeMs() {
        return searchTimeMs;
    }
    
    public void setSearchTimeMs(Long searchTimeMs) {
        this.searchTimeMs = searchTimeMs;
    }
    
    public String getSearchQuery() {
        return searchQuery;
    }
    
    public void setSearchQuery(String searchQuery) {
        this.searchQuery = searchQuery;
    }
}
