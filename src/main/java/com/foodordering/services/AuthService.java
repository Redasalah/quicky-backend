// src/main/java/com/foodordering/services/AuthService.java
package com.foodordering.services;

import com.foodordering.dtos.requests.LoginRequest;
import com.foodordering.dtos.requests.RegisterRequest;
import com.foodordering.dtos.responses.ApiResponse;
import com.foodordering.dtos.responses.AuthResponse;
import com.foodordering.exceptions.BadRequestException;
import com.foodordering.models.Role;
import com.foodordering.models.User;
import com.foodordering.repositories.UserRepository;
import com.foodordering.security.JwtTokenProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenProvider tokenProvider;

    public AuthResponse login(LoginRequest loginRequest) {
        // This creates the authentication and throws an exception if credentials are invalid
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()
                )
        );
    
        String token = tokenProvider.generateToken(loginRequest.getEmail());
        User user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new BadRequestException("User not found"));
    
        return new AuthResponse(
                token,
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRole()
        );
    }

    public ApiResponse register(RegisterRequest registerRequest) {
        logger.info("Attempting to register user: {}", registerRequest.getEmail());

        // Check if email already exists
        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            logger.warn("Registration failed: Email already exists - {}", registerRequest.getEmail());
            throw new BadRequestException("Email is already taken");
        }

        // Create new user
        User user = new User();
        user.setName(registerRequest.getFirstName() + " " + registerRequest.getLastName());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        
        // Convert and validate role
        try {
            user.setRole(Role.valueOf(registerRequest.getRole()));
        } catch (IllegalArgumentException e) {
            logger.error("Invalid role during registration: {}", registerRequest.getRole());
            throw new BadRequestException("Invalid role: " + registerRequest.getRole());
        }

        // Save user
        userRepository.save(user);

        logger.info("User registered successfully: {}", registerRequest.getEmail());
        return new ApiResponse(true, "User registered successfully");
    }
}