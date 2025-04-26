// src/main/java/com/foodordering/controllers/AuthController.java
package com.foodordering.controllers;

import com.foodordering.dtos.requests.LoginRequest;
import com.foodordering.dtos.requests.RegisterRequest;
import com.foodordering.dtos.responses.ApiResponse;
import com.foodordering.dtos.responses.AuthResponse;
import com.foodordering.services.AuthService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        logger.info("Received login request for email: {}", loginRequest.getEmail());
        try {
            AuthResponse response = authService.login(loginRequest);
            logger.info("Login successful for email: {}", loginRequest.getEmail());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Login error", e);
            throw e; // Letting global exception handler manage this
        }
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse> register(@Valid @RequestBody RegisterRequest registerRequest) {
        logger.info("Received registration request for email: {}", registerRequest.getEmail());
        try {
            ApiResponse response = authService.register(registerRequest);
            logger.info("Registration response: {}", response);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Registration error", e);
            return ResponseEntity.badRequest().body(
                new ApiResponse(false, "Registration failed: " + e.getMessage())
            );
        }
    }
}