package com.foodordering.backend.services;

import com.foodordering.backend.models.Restaurant;
import com.foodordering.backend.repositories.RestaurantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class RestaurantService {
    
    private final RestaurantRepository restaurantRepository;
    
    @Autowired
    public RestaurantService(RestaurantRepository restaurantRepository) {
        this.restaurantRepository = restaurantRepository;
    }
    
    public List<Restaurant> getAllRestaurants() {
        return restaurantRepository.findByIsActiveTrue();
    }
    
    public Optional<Restaurant> getRestaurantById(Long id) {
        return restaurantRepository.findById(id);
    }
    
    public List<Restaurant> getRestaurantsByCuisine(String cuisine) {
        return restaurantRepository.findByCuisine(cuisine);
    }
    
    public List<Restaurant> searchRestaurantsByName(String name) {
        return restaurantRepository.findByNameContainingIgnoreCase(name);
    }
    
    @Transactional
    public Restaurant createRestaurant(Restaurant restaurant) {
        return restaurantRepository.save(restaurant);
    }
    
    @Transactional
    public Restaurant updateRestaurant(Long id, Restaurant restaurant) {
        return restaurantRepository.findById(id)
                .map(existingRestaurant -> {
                    existingRestaurant.setName(restaurant.getName());
                    existingRestaurant.setCuisine(restaurant.getCuisine());
                    existingRestaurant.setDescription(restaurant.getDescription());
                    existingRestaurant.setImageUrl(restaurant.getImageUrl());
                    existingRestaurant.setRating(restaurant.getRating());
                    existingRestaurant.setDeliveryTime(restaurant.getDeliveryTime());
                    existingRestaurant.setDeliveryFee(restaurant.getDeliveryFee());
                    existingRestaurant.setPriceRange(restaurant.getPriceRange());
                    existingRestaurant.setAddress(restaurant.getAddress());
                    existingRestaurant.setCity(restaurant.getCity());
                    existingRestaurant.setPostalCode(restaurant.getPostalCode());
                    existingRestaurant.setIsActive(restaurant.getIsActive());
                    return restaurantRepository.save(existingRestaurant);
                })
                .orElseThrow(() -> new RuntimeException("Restaurant not found with id: " + id));
    }
    
    @Transactional
    public void deleteRestaurant(Long id) {
        restaurantRepository.deleteById(id);
    }
}