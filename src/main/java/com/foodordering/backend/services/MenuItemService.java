package com.foodordering.backend.services;

import com.foodordering.backend.models.MenuItem;
import com.foodordering.backend.models.Restaurant;
import com.foodordering.backend.repositories.MenuItemRepository;
import com.foodordering.backend.repositories.RestaurantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class MenuItemService {
    
    private final MenuItemRepository menuItemRepository;
    private final RestaurantRepository restaurantRepository;
    
    @Autowired
    public MenuItemService(MenuItemRepository menuItemRepository, RestaurantRepository restaurantRepository) {
        this.menuItemRepository = menuItemRepository;
        this.restaurantRepository = restaurantRepository;
    }
    
    public List<MenuItem> getAllMenuItems() {
        return menuItemRepository.findAll();
    }
    
    public Optional<MenuItem> getMenuItemById(Long id) {
        return menuItemRepository.findById(id);
    }
    
    public List<MenuItem> getMenuItemsByRestaurantId(Long restaurantId) {
        return menuItemRepository.findByRestaurantId(restaurantId);
    }
    
    public List<MenuItem> getMenuItemsByCategory(Long restaurantId, String category) {
        return menuItemRepository.findByRestaurantIdAndCategory(restaurantId, category);
    }
    
    public List<MenuItem> getAvailableMenuItems(Long restaurantId) {
        return menuItemRepository.findByRestaurantIdAndAvailableTrue(restaurantId);
    }
    
    @Transactional
    public MenuItem createMenuItem(MenuItem menuItem, Long restaurantId) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new RuntimeException("Restaurant not found with id: " + restaurantId));
        
        menuItem.setRestaurant(restaurant);
        return menuItemRepository.save(menuItem);
    }
    
    @Transactional
    public MenuItem updateMenuItem(Long id, MenuItem menuItem) {
        return menuItemRepository.findById(id)
                .map(existingItem -> {
                    existingItem.setName(menuItem.getName());
                    existingItem.setDescription(menuItem.getDescription());
                    existingItem.setPrice(menuItem.getPrice());
                    existingItem.setImageUrl(menuItem.getImageUrl());
                    existingItem.setCategory(menuItem.getCategory());
                    existingItem.setPopular(menuItem.getPopular());
                    existingItem.setAvailable(menuItem.getAvailable());
                    return menuItemRepository.save(existingItem);
                })
                .orElseThrow(() -> new RuntimeException("Menu item not found with id: " + id));
    }
    
    @Transactional
    public void deleteMenuItem(Long id) {
        menuItemRepository.deleteById(id);
    }
}