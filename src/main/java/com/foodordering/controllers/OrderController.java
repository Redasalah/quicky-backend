// src/main/java/com/foodordering/controllers/OrderController.java
package com.foodordering.controllers;

import com.foodordering.dtos.requests.OrderRequest;
import com.foodordering.dtos.responses.OrderResponse;
import com.foodordering.models.OrderStatus;
import com.foodordering.services.OrderService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<OrderResponse> createOrder(@Valid @RequestBody OrderRequest orderRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(orderService.createOrder(orderRequest));
    }

    @GetMapping("/me")
    public ResponseEntity<List<OrderResponse>> getCurrentUserOrders() {
        return ResponseEntity.ok(orderService.getCurrentUserOrders());
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> getOrderById(@PathVariable Long id) {
        return ResponseEntity.ok(orderService.getOrderById(id));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<OrderResponse> updateOrderStatus(@PathVariable Long id, @RequestParam OrderStatus status) {
        return ResponseEntity.ok(orderService.updateOrderStatus(id, status));
    }

    @PutMapping("/{id}/assign")
    @PreAuthorize("hasRole('DELIVERY_PERSONNEL')")
    public ResponseEntity<OrderResponse> assignDeliveryPerson(@PathVariable Long id) {
        return ResponseEntity.ok(orderService.assignDeliveryPerson(id));
    }
}