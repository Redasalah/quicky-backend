// src/main/java/com/foodordering/repositories/RestaurantRepository.java
package com.foodordering.repositories;

import com.foodordering.models.Restaurant;
import com.foodordering.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {
    
    List<Restaurant> findByActive(boolean active);
    
    List<Restaurant> findByCuisine(String cuisine);
    
    Optional<Restaurant> findByOwner(User owner);
    
    List<Restaurant> findByNameContainingIgnoreCase(String name);
}