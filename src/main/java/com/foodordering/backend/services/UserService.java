package com.foodordering.backend.services;

import com.foodordering.backend.models.User;
import com.foodordering.backend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    
    private final UserRepository userRepository;
    
    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    
    /**
     * Find all users in the system.
     */
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }
    
    /**
     * Find a user by their ID.
     */
    public Optional<User> findUserById(Long id) {
        return userRepository.findById(id);
    }
    
    /**
     * Find a user by their email.
     */
    public Optional<User> findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }
    
    /**
     * Create a new user.
     */
    @Transactional
    public User createUser(User user) {
        // Check if email already exists
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("Email already exists: " + user.getEmail());
        }
        
        // In a real application, you would encrypt the password here
        // For now, we'll store it as is for simplicity
        
        return userRepository.save(user);
    }
    
    /**
     * Update an existing user.
     */
    @Transactional
    public User updateUser(Long id, User userDetails) {
        return userRepository.findById(id)
            .map(existingUser -> {
                // Update fields
                existingUser.setFirstName(userDetails.getFirstName());
                existingUser.setLastName(userDetails.getLastName());
                existingUser.setPhoneNumber(userDetails.getPhoneNumber());
                existingUser.setAddress(userDetails.getAddress());
                existingUser.setCity(userDetails.getCity());
                existingUser.setPostalCode(userDetails.getPostalCode());
                
                // Don't update email or password here
                // Those should have separate methods with proper validation
                
                return userRepository.save(existingUser);
            })
            .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
    }
    
    /**
     * Delete a user by their ID.
     */
    @Transactional
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}