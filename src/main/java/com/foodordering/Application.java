// src/main/java/com/foodordering/Application.java
package com.foodordering;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {
    private static final Logger logger = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) {
        try {
            logger.info("===== Starting Food Ordering Application =====");
            logger.info("Initializing Spring Boot context...");
            
            SpringApplication.run(Application.class, args);
            
            logger.info("===== Application Started Successfully =====");
            logger.info("Application is running on port 8080");
        } catch (Exception e) {
            logger.error("===== Application Startup Failed =====", e);
        }
    }
}