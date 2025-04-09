package com.foodordering.backend.config;

import com.foodordering.backend.models.Role;
import com.foodordering.backend.models.User;
import com.foodordering.backend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;

    @Autowired
    public DataInitializer(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void run(String... args) {
        // Create test users if the database is empty
        if (userRepository.count() == 0) {
            // Create a customer
            User customer = new User();
            customer.setFirstName("John");
            customer.setLastName("Doe");
            customer.setEmail("john.doe@example.com");
            customer.setPassword("password123"); // In a real app, encode this
            customer.setRole(Role.CUSTOMER);
            customer.setPhoneNumber("123-456-7890");
            customer.setAddress("123 Main St");
            customer.setCity("Anytown");
            customer.setPostalCode("12345");
            userRepository.save(customer);

            // Create a restaurant staff
            User restaurantStaff = new User();
            restaurantStaff.setFirstName("Jane");
            restaurantStaff.setLastName("Smith");
            restaurantStaff.setEmail("reda@gmail.com");
            restaurantStaff.setPassword("123456"); // In a real app, encode this
            restaurantStaff.setRole(Role.RESTAURANT_STAFF);
            restaurantStaff.setPhoneNumber("098-765-4321");
            userRepository.save(restaurantStaff);

            // Create a delivery person
            User deliveryPerson = new User();
            deliveryPerson.setFirstName("Mike");
            deliveryPerson.setLastName("Johnson");
            deliveryPerson.setEmail("mike.johnson@example.com");
            deliveryPerson.setPassword("password123"); // In a real app, encode this
            deliveryPerson.setRole(Role.DELIVERY_PERSONNEL);
            deliveryPerson.setPhoneNumber("555-123-4567");
            userRepository.save(deliveryPerson);

            System.out.println("Sample users created successfully!");
        }
    }
}