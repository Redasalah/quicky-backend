// src/main/java/com/foodordering/controllers/PublicController.java
package com.foodordering.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/public")
public class PublicController {
    private static final Logger logger = LoggerFactory.getLogger(PublicController.class);

    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> healthCheck() {
        logger.info("=== Health Check Endpoint Accessed ===");
        logger.info("Timestamp: {}", LocalDateTime.now());
        
        Map<String, Object> response = new HashMap<>();
        response.put("status", "UP");
        response.put("timestamp", LocalDateTime.now().toString());
        response.put("message", "Food Ordering System is running successfully");
        
        logger.info("Health Check Response: {}", response);
        
        return ResponseEntity.ok(response);
    }
}