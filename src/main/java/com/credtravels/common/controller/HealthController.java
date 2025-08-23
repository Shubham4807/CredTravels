package com.credtravels.common.controller;

import com.credtravels.common.dto.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/health")
public class HealthController {
    
    @GetMapping
    public ResponseEntity<ApiResponse<Map<String, Object>>> healthCheck() {
        Map<String, Object> healthInfo = new HashMap<>();
        healthInfo.put("status", "UP");
        healthInfo.put("timestamp", LocalDateTime.now());
        healthInfo.put("service", "CredTravels");
        healthInfo.put("version", "1.0.0");
        
        return ResponseEntity.ok(ApiResponse.success("Service is healthy", healthInfo));
    }
    
    @GetMapping("/ping")
    public ResponseEntity<String> ping() {
        return ResponseEntity.ok("pong");
    }
}
