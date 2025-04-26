// src/main/java/com/foodordering/exceptions/GlobalExceptionHandler.java
package com.foodordering.exceptions;

import com.foodordering.dtos.responses.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse> handleGenericException(Exception ex) {
        // Log the full stack trace
        logger.error("Unexpected error occurred", ex);
        
        // Extract detailed error information
        String errorMessage = "An unexpected error occurred";
        String detailedMessage = ex.getMessage();
        
        // Log additional context
        logger.error("Exception class: {}", ex.getClass().getName());
        logger.error("Detailed message: {}", detailedMessage);
        
        // Log cause if available
        if (ex.getCause() != null) {
            logger.error("Cause: {}", ex.getCause().getMessage());
            logger.error("Cause class: {}", ex.getCause().getClass().getName());
        }
        
        // Print full stack trace to console
        ex.printStackTrace();
        
        return new ResponseEntity<>(
            new ApiResponse(false, detailedMessage != null ? detailedMessage : errorMessage), 
            HttpStatus.INTERNAL_SERVER_ERROR
        );
    }
    
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
            logger.error("Validation Error - Field: {}, Message: {}", fieldName, errorMessage);
        });
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }
}