// src/main/java/com/foodordering/services/MenuItemService.java
package com.foodordering.services;

import com.foodordering.dtos.requests.MenuItemRequest;
import com.foodordering.dtos.responses.MenuItemDto;
import com.foodordering.exceptions.ResourceNotFoundException;
import com.foodordering.models.MenuItem;
import com.foodordering.models.Restaurant;
import com.foodordering.models.User;
import com.foodordering.repositories.MenuItemRepository;
import com.foodordering.repositories.RestaurantRepository;
import com.foodordering.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MenuItemService {
    
    @Autowired
    private MenuItemRepository menuItemRepository;
    
    @Autowired
    private RestaurantRepository restaurantRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    // Get all menu items for a restaurant
    public List<MenuItemDto> getMenuItemsByRestaurant(Long restaurantId) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found with id: " + restaurantId));
        
        List<MenuItem> menuItems = menuItemRepository.findByRestaurant(restaurant);
        
        return menuItems.stream()
                .map(this::convertToMenuItemDto)
                .collect(Collectors.toList());
    }
    
    // Create a new menu item
    public MenuItemDto createMenuItem(MenuItemRequest menuItemRequest) {
        // Get the currently authenticated user
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User owner = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));
        
        // Find the restaurant
        Restaurant restaurant = restaurantRepository.findById(menuItemRequest.getRestaurantId())
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found with id: " + menuItemRequest.getRestaurantId()));
        
        // Check if the user is the owner of the restaurant
        if (!restaurant.getOwner().getId().equals(owner.getId())) {
            throw new RuntimeException("You are not authorized to add menu items to this restaurant");
        }
        
        // Create new menu item
        MenuItem menuItem = new MenuItem();
        menuItem.setName(menuItemRequest.getName());
        menuItem.setDescription(menuItemRequest.getDescription());
        menuItem.setPrice(menuItemRequest.getPrice());
        menuItem.setImageUrl(menuItemRequest.getImageUrl());
        menuItem.setCategory(menuItemRequest.getCategory());
        menuItem.setAvailable(menuItemRequest.isAvailable());
        menuItem.setPopular(menuItemRequest.isPopular());
        menuItem.setRestaurant(restaurant);
        
        // Save the menu item
        MenuItem savedMenuItem = menuItemRepository.save(menuItem);
        
        return convertToMenuItemDto(savedMenuItem);
    }
    
    // Update an existing menu item
    public MenuItemDto updateMenuItem(Long id, MenuItemRequest menuItemRequest) {
        // Get the currently authenticated user
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User owner = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));
        
        // Find the menu item
        MenuItem menuItem = menuItemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Menu item not found with id: " + id));
        
        // Check if the user is the owner of the restaurant
        if (!menuItem.getRestaurant().getOwner().getId().equals(owner.getId())) {
            throw new RuntimeException("You are not authorized to update menu items for this restaurant");
        }
        
        // Update menu item details
        menuItem.setName(menuItemRequest.getName());
        menuItem.setDescription(menuItemRequest.getDescription());
        menuItem.setPrice(menuItemRequest.getPrice());
        menuItem.setImageUrl(menuItemRequest.getImageUrl());
        menuItem.setCategory(menuItemRequest.getCategory());
        menuItem.setAvailable(menuItemRequest.isAvailable());
        menuItem.setPopular(menuItemRequest.isPopular());
        
        // Save the updated menu item
        MenuItem updatedMenuItem = menuItemRepository.save(menuItem);
        
        return convertToMenuItemDto(updatedMenuItem);
    }
    
    // Delete a menu item
    public void deleteMenuItem(Long id) {
        // Get the currently authenticated user
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User owner = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));
        
        // Find the menu item
        MenuItem menuItem = menuItemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Menu item not found with id: " + id));
        
        // Check if the user is the owner of the restaurant
        if (!menuItem.getRestaurant().getOwner().getId().equals(owner.getId())) {
            throw new RuntimeException("You are not authorized to delete menu items for this restaurant");
        }
        
        // Delete the menu item
        menuItemRepository.delete(menuItem);
    }
    
    // Helper method to convert MenuItem entity to MenuItemDto
    private MenuItemDto convertToMenuItemDto(MenuItem menuItem) {
        MenuItemDto dto = new MenuItemDto();
        
        dto.setId(menuItem.getId());
        dto.setName(menuItem.getName());
        dto.setDescription(menuItem.getDescription());
        dto.setPrice(menuItem.getPrice());
        dto.setImageUrl(menuItem.getImageUrl());
        dto.setCategory(menuItem.getCategory());
        dto.setAvailable(menuItem.isAvailable());
        dto.setPopular(menuItem.isPopular());
        dto.setRestaurantId(menuItem.getRestaurant().getId());
        
        return dto;
    }
}