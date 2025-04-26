// src/main/java/com/foodordering/repositories/OrderRepository.java
package com.foodordering.repositories;

import com.foodordering.models.Order;
import com.foodordering.models.OrderStatus;
import com.foodordering.models.Restaurant;
import com.foodordering.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    
    List<Order> findByCustomer(User customer);
    
    List<Order> findByRestaurant(Restaurant restaurant);
    
    List<Order> findByDeliveryPerson(User deliveryPerson);
    
    List<Order> findByStatus(OrderStatus status);
    
    List<Order> findByRestaurantAndStatus(Restaurant restaurant, OrderStatus status);
    
    Optional<Order> findByOrderNumber(String orderNumber);
}