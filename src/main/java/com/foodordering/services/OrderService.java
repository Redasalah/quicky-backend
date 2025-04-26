// src/main/java/com/foodordering/services/OrderService.java
package com.foodordering.services;

import com.foodordering.dtos.requests.OrderRequest;
import com.foodordering.dtos.responses.OrderResponse;
import com.foodordering.dtos.responses.RestaurantResponse;
import com.foodordering.exceptions.ResourceNotFoundException;
import com.foodordering.models.*;
import com.foodordering.repositories.MenuItemRepository;
import com.foodordering.repositories.OrderRepository;
import com.foodordering.repositories.RestaurantRepository;
import com.foodordering.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class OrderService {
    
    @Autowired
    private OrderRepository orderRepository;
    
    @Autowired
    private RestaurantRepository restaurantRepository;
    
    @Autowired
    private MenuItemRepository menuItemRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    // Create a new order
    @Transactional
    public OrderResponse createOrder(OrderRequest orderRequest) {
        // Get the currently authenticated user
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User customer = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));
        
        // Find the restaurant
        Restaurant restaurant = restaurantRepository.findById(orderRequest.getRestaurantId())
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found with id: " + orderRequest.getRestaurantId()));
        
        // Create new order
        Order order = new Order();
        order.setOrderNumber("ORD-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        order.setCustomer(customer);
        order.setRestaurant(restaurant);
        order.setStatus(OrderStatus.PENDING);
        order.setOrderTime(LocalDateTime.now());
        
        // Calculate estimated delivery time (30 minutes from now)
        order.setEstimatedDeliveryTime(LocalDateTime.now().plusMinutes(30));
        
        // Set payment method
        order.setPaymentMethod(orderRequest.getPaymentMethod());
        order.setPaymentStatus("PAID"); // Assume payment is successful for now
        
        // Create delivery address
        if (orderRequest.getStreet() != null && orderRequest.getCity() != null && orderRequest.getPostalCode() != null) {
            Address deliveryAddress = new Address();
            deliveryAddress.setStreet(orderRequest.getStreet());
            deliveryAddress.setCity(orderRequest.getCity());
            deliveryAddress.setPostalCode(orderRequest.getPostalCode());
            
            order.setDeliveryAddress(deliveryAddress);
        }
        
        // Add items to order
        double subtotal = 0.0;
        
        for (OrderRequest.OrderItemRequest itemRequest : orderRequest.getItems()) {
            MenuItem menuItem = menuItemRepository.findById(itemRequest.getMenuItemId())
                    .orElseThrow(() -> new ResourceNotFoundException("Menu item not found with id: " + itemRequest.getMenuItemId()));
            
            OrderItem orderItem = new OrderItem();
            orderItem.setMenuItem(menuItem);
            orderItem.setQuantity(itemRequest.getQuantity());
            orderItem.setPrice(menuItem.getPrice());
            
            order.addItem(orderItem);
            
            // Calculate item total and add to subtotal
            subtotal += menuItem.getPrice() * itemRequest.getQuantity();
        }
        
        // Set order totals
        order.setSubtotal(subtotal);
        order.setDeliveryFee(restaurant.getDeliveryFee());
        order.setTax(subtotal * 0.08); // Assume 8% tax
        order.setTotal(subtotal + order.getDeliveryFee() + order.getTax());
        
        // Save the order
        Order savedOrder = orderRepository.save(order);
        
        return convertToOrderResponse(savedOrder);
    }
    
    // Get orders for the current user
    public List<OrderResponse> getCurrentUserOrders() {
        // Get the currently authenticated user
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User customer = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));
        
        List<Order> orders = orderRepository.findByCustomer(customer);
        
        return orders.stream()
                .map(this::convertToOrderResponse)
                .collect(Collectors.toList());
    }
    
    // Get order by ID
    public OrderResponse getOrderById(Long id) {
        // Get the currently authenticated user
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));
        
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + id));
        
        // Check if the user is the customer, the restaurant owner, or the delivery person
        if (!order.getCustomer().getId().equals(user.getId()) && 
            !order.getRestaurant().getOwner().getId().equals(user.getId()) &&
            (order.getDeliveryPerson() == null || !order.getDeliveryPerson().getId().equals(user.getId()))) {
            throw new RuntimeException("You are not authorized to view this order");
        }
        
        return convertToOrderResponse(order);
    }
    
    // Update order status (for restaurant staff)
    @Transactional
    public OrderResponse updateOrderStatus(Long id, OrderStatus status) {
        // Get the currently authenticated user
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));
        
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + id));
        
        // Check if the user is the restaurant owner or a delivery person (for specific statuses)
        if (!order.getRestaurant().getOwner().getId().equals(user.getId()) && 
            !(order.getDeliveryPerson() != null && order.getDeliveryPerson().getId().equals(user.getId()) && 
              (status == OrderStatus.OUT_FOR_DELIVERY || status == OrderStatus.DELIVERED))) {
            throw new RuntimeException("You are not authorized to update this order's status");
        }
        
        // Update the order status
        order.setStatus(status);
        
        // If the order is delivered, set the delivery time to now
        if (status == OrderStatus.DELIVERED) {
            order.setEstimatedDeliveryTime(LocalDateTime.now());
        }
        
        // Save the updated order
        Order updatedOrder = orderRepository.save(order);
        
        return convertToOrderResponse(updatedOrder);
    }
    
    // Assign delivery person to an order (for delivery personnel)
    @Transactional
    public OrderResponse assignDeliveryPerson(Long id) {
        // Get the currently authenticated user (delivery person)
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User deliveryPerson = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));
        
        // Check if the user is a delivery person
        if (deliveryPerson.getRole() != Role.DELIVERY_PERSONNEL) {
            throw new RuntimeException("Only delivery personnel can accept orders for delivery");
        }
        
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + id));
        
        // Check if the order is ready for delivery
        if (order.getStatus() != OrderStatus.READY_FOR_DELIVERY) {
            throw new RuntimeException("This order is not ready for delivery yet");
        }
        
        // Check if the order already has a delivery person assigned
        if (order.getDeliveryPerson() != null) {
            throw new RuntimeException("This order already has a delivery person assigned");
        }
        
        // Assign the delivery person to the order
        order.setDeliveryPerson(deliveryPerson);
        order.setStatus(OrderStatus.OUT_FOR_DELIVERY);
        
        // Save the updated order
        Order updatedOrder = orderRepository.save(order);
        
        return convertToOrderResponse(updatedOrder);
    }
    
    // Helper method to convert Order entity to OrderResponse
    private OrderResponse convertToOrderResponse(Order order) {
        OrderResponse response = new OrderResponse();
        
        response.setId(order.getId());
        response.setOrderNumber(order.getOrderNumber());
        response.setCustomerId(order.getCustomer().getId());
        response.setCustomerName(order.getCustomer().getName());
        
        // Set restaurant info
        RestaurantResponse restaurantResponse = new RestaurantResponse();
        restaurantResponse.setId(order.getRestaurant().getId());
        restaurantResponse.setName(order.getRestaurant().getName());
        restaurantResponse.setImageUrl(order.getRestaurant().getImageUrl());
        response.setRestaurant(restaurantResponse);
        
        // Set order items
        List<OrderResponse.OrderItemDto> itemDtos = new ArrayList<>();
        for (OrderItem item : order.getItems()) {
            OrderResponse.OrderItemDto itemDto = new OrderResponse.OrderItemDto();
            itemDto.setId(item.getId());
            itemDto.setName(item.getMenuItem().getName());
            itemDto.setQuantity(item.getQuantity());
            itemDto.setPrice(item.getPrice());
            itemDto.setTotalPrice(item.getPrice() * item.getQuantity());
            
            itemDtos.add(itemDto);
        }
        response.setItems(itemDtos);
        
        // Set delivery address
        if (order.getDeliveryAddress() != null) {
            Address address = order.getDeliveryAddress();
            response.setDeliveryAddress(address.getStreet() + ", " + address.getCity() + ", " + address.getPostalCode());
        }
        
        response.setStatus(order.getStatus());
        response.setSubtotal(order.getSubtotal());
        response.setDeliveryFee(order.getDeliveryFee());
        response.setTax(order.getTax());
        response.setTotal(order.getTotal());
        response.setOrderTime(order.getOrderTime());
        response.setEstimatedDeliveryTime(order.getEstimatedDeliveryTime());
        response.setPaymentMethod(order.getPaymentMethod());
        response.setPaymentStatus(order.getPaymentStatus());
        
        return response;
    }
}