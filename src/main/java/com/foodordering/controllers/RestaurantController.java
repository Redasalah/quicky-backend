// src/main/java/com/foodordering/controllers/RestaurantController.java
package com.foodordering.controllers;

import com.foodordering.dtos.requests.RestaurantRequest;
import com.foodordering.dtos.responses.RestaurantResponse;
import com.foodordering.services.RestaurantService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/restaurants")
public class RestaurantController {

    @Autowired
    private RestaurantService restaurantService;

    @GetMapping
    public ResponseEntity<List<RestaurantResponse>> getAllRestaurants() {
        return ResponseEntity.ok(restaurantService.getAllRestaurants());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RestaurantResponse> getRestaurantById(@PathVariable Long id) {
        return ResponseEntity.ok(restaurantService.getRestaurantById(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('RESTAURANT_STAFF')")
    public ResponseEntity<RestaurantResponse> createRestaurant(@Valid @RequestBody RestaurantRequest restaurantRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(restaurantService.createRestaurant(restaurantRequest));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('RESTAURANT_STAFF')")
    public ResponseEntity<RestaurantResponse> updateRestaurant(@PathVariable Long id, @Valid @RequestBody RestaurantRequest restaurantRequest) {
        return ResponseEntity.ok(restaurantService.updateRestaurant(id, restaurantRequest));
    }
}