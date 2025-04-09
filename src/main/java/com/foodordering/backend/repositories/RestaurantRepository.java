package com.foodordering.backend.repositories;

import com.foodordering.backend.models.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {
    List<Restaurant> findByCuisine(String cuisine);
    List<Restaurant> findByNameContainingIgnoreCase(String name);
    List<Restaurant> findByPriceRange(String priceRange);
    List<Restaurant> findByIsActiveTrue();
}