// src/main/java/com/foodordering/dtos/requests/RestaurantRequest.java
package com.foodordering.dtos.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public class RestaurantRequest {
    
    @NotBlank(message = "Name is required")
    private String name;
    
    private String description;
    
    private String imageUrl;
    
    @NotBlank(message = "Cuisine is required")
    private String cuisine;
    
    @NotBlank(message = "Street is required")
    private String street;
    
    @NotBlank(message = "City is required")
    private String city;
    
    @NotBlank(message = "Postal code is required")
    private String postalCode;
    
    private String priceRange;
    
    @Positive(message = "Delivery fee must be positive")
    private Double deliveryFee;
    
    private String deliveryTime;
    
    private String openingTime;
    
    private String closingTime;
    
    // Getters and Setters
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getImageUrl() {
        return imageUrl;
    }
    
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
    
    public String getCuisine() {
        return cuisine;
    }
    
    public void setCuisine(String cuisine) {
        this.cuisine = cuisine;
    }
    
    public String getStreet() {
        return street;
    }
    
    public void setStreet(String street) {
        this.street = street;
    }
    
    public String getCity() {
        return city;
    }
    
    public void setCity(String city) {
        this.city = city;
    }
    
    public String getPostalCode() {
        return postalCode;
    }
    
    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }
    
    public String getPriceRange() {
        return priceRange;
    }
    
    public void setPriceRange(String priceRange) {
        this.priceRange = priceRange;
    }
    
    public Double getDeliveryFee() {
        return deliveryFee;
    }
    
    public void setDeliveryFee(Double deliveryFee) {
        this.deliveryFee = deliveryFee;
    }
    
    public String getDeliveryTime() {
        return deliveryTime;
    }
    
    public void setDeliveryTime(String deliveryTime) {
        this.deliveryTime = deliveryTime;
    }
    
    public String getOpeningTime() {
        return openingTime;
    }
    
    public void setOpeningTime(String openingTime) {
        this.openingTime = openingTime;
    }
    
    public String getClosingTime() {
        return closingTime;
    }
    
    public void setClosingTime(String closingTime) {
        this.closingTime = closingTime;
    }
}