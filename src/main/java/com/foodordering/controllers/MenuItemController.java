// src/main/java/com/foodordering/controllers/MenuItemController.java
package com.foodordering.controllers;

import com.foodordering.dtos.requests.MenuItemRequest;
import com.foodordering.dtos.responses.MenuItemDto;
import com.foodordering.services.MenuItemService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/menu-items")
public class MenuItemController {

    @Autowired
    private MenuItemService menuItemService;

    @GetMapping("/restaurant/{restaurantId}")
    public ResponseEntity<List<MenuItemDto>> getMenuItemsByRestaurant(@PathVariable Long restaurantId) {
        return ResponseEntity.ok(menuItemService.getMenuItemsByRestaurant(restaurantId));
    }

    @PostMapping
    @PreAuthorize("hasRole('RESTAURANT_STAFF')")
    public ResponseEntity<MenuItemDto> createMenuItem(@Valid @RequestBody MenuItemRequest menuItemRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(menuItemService.createMenuItem(menuItemRequest));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('RESTAURANT_STAFF')")
    public ResponseEntity<MenuItemDto> updateMenuItem(@PathVariable Long id, @Valid @RequestBody MenuItemRequest menuItemRequest) {
        return ResponseEntity.ok(menuItemService.updateMenuItem(id, menuItemRequest));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('RESTAURANT_STAFF')")
    public ResponseEntity<Void> deleteMenuItem(@PathVariable Long id) {
        menuItemService.deleteMenuItem(id);
        return ResponseEntity.noContent().build();
    }
}