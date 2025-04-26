// src/main/java/com/foodordering/services/RestaurantService.java
package com.foodordering.services;

import com.foodordering.dtos.requests.RestaurantRequest;
import com.foodordering.dtos.responses.RestaurantResponse;
import com.foodordering.exceptions.ResourceNotFoundException;
import com.foodordering.models.Address;
import com.foodordering.models.Restaurant;
import com.foodordering.models.User;
import com.foodordering.repositories.RestaurantRepository;
import com.foodordering.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RestaurantService {
    
    @Autowired
    private RestaurantRepository restaurantRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    // Get all restaurants
    public List<RestaurantResponse> getAllRestaurants() {
        List<Restaurant> restaurants = restaurantRepository.findByActive(true);
        return restaurants.stream()
                .map(this::convertToRestaurantResponse)
                .collect(Collectors.toList());
    }
    
    // Get restaurant by ID
    public RestaurantResponse getRestaurantById(Long id) {
        Restaurant restaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found with id: " + id));
        
        return convertToRestaurantResponse(restaurant);
    }
    
    // Create a new restaurant
    public RestaurantResponse createRestaurant(RestaurantRequest restaurantRequest) {
        // Get the currently authenticated user
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User owner = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));
        
        // Create new restaurant
        Restaurant restaurant = new Restaurant();
        restaurant.setName(restaurantRequest.getName());
        restaurant.setDescription(restaurantRequest.getDescription());
        restaurant.setImageUrl(restaurantRequest.getImageUrl());
        restaurant.setCuisine(restaurantRequest.getCuisine());
        restaurant.setPriceRange(restaurantRequest.getPriceRange());
        restaurant.setDeliveryFee(restaurantRequest.getDeliveryFee());
        restaurant.setDeliveryTime(restaurantRequest.getDeliveryTime());
        restaurant.setOwner(owner);
        
        // Create and set address
        Address address = new Address();
        address.setStreet(restaurantRequest.getStreet());
        address.setCity(restaurantRequest.getCity());
        address.setPostalCode(restaurantRequest.getPostalCode());
        
        restaurant.setAddress(address);
        
        // Save the restaurant
        Restaurant savedRestaurant = restaurantRepository.save(restaurant);
        
        return convertToRestaurantResponse(savedRestaurant);
    }
    
    // Update an existing restaurant
    public RestaurantResponse updateRestaurant(Long id, RestaurantRequest restaurantRequest) {
        // Get the currently authenticated user
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User owner = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));
        
        // Find the restaurant
        Restaurant restaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found with id: " + id));
        
        // Check if the user is the owner of the restaurant
        if (!restaurant.getOwner().getId().equals(owner.getId())) {
            throw new RuntimeException("You are not authorized to update this restaurant");
        }
        
        // Update restaurant details
        restaurant.setName(restaurantRequest.getName());
        restaurant.setDescription(restaurantRequest.getDescription());
        restaurant.setImageUrl(restaurantRequest.getImageUrl());
        restaurant.setCuisine(restaurantRequest.getCuisine());
        restaurant.setPriceRange(restaurantRequest.getPriceRange());
        restaurant.setDeliveryFee(restaurantRequest.getDeliveryFee());
        restaurant.setDeliveryTime(restaurantRequest.getDeliveryTime());
        
        // Update address
        Address address = restaurant.getAddress();
        if (address == null) {
            address = new Address();
            restaurant.setAddress(address);
        }
        
        address.setStreet(restaurantRequest.getStreet());
        address.setCity(restaurantRequest.getCity());
        address.setPostalCode(restaurantRequest.getPostalCode());
        
        // Save the updated restaurant
        Restaurant updatedRestaurant = restaurantRepository.save(restaurant);
        
        return convertToRestaurantResponse(updatedRestaurant);
    }
    
    // Helper method to convert Restaurant entity to RestaurantResponse
    private RestaurantResponse convertToRestaurantResponse(Restaurant restaurant) {
        RestaurantResponse response = new RestaurantResponse();
        
        response.setId(restaurant.getId());
        response.setName(restaurant.getName());
        response.setDescription(restaurant.getDescription());
        response.setImageUrl(restaurant.getImageUrl());
        response.setCuisine(restaurant.getCuisine());
        
        // Format the address
        if (restaurant.getAddress() != null) {
            Address address = restaurant.getAddress();
            response.setAddress(address.getStreet() + ", " + address.getCity() + ", " + address.getPostalCode());
        }
        
        response.setRating(restaurant.getRating());
        response.setPriceRange(restaurant.getPriceRange());
        response.setDeliveryFee(restaurant.getDeliveryFee());
        response.setDeliveryTime(restaurant.getDeliveryTime());
        response.setActive(restaurant.isActive());
        
        return response;
    }
}