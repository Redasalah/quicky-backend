// src/main/java/com/foodordering/dtos/responses/RestaurantResponse.java
package com.foodordering.dtos.responses;

public class RestaurantResponse {
    
    private Long id;
    private String name;
    private String description;
    private String imageUrl;
    private String cuisine;
    private String address;
    private Double rating;
    private String priceRange;
    private Double deliveryFee;
    private String deliveryTime;
    private String openingTime;
    private String closingTime;
    private boolean active;
    
    // Default constructor
    public RestaurantResponse() {
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
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
    
    public String getAddress() {
        return address;
    }
    
    public void setAddress(String address) {
        this.address = address;
    }
    
    public Double getRating() {
        return rating;
    }
    
    public void setRating(Double rating) {
        this.rating = rating;
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
    
    public boolean isActive() {
        return active;
    }
    
    public void setActive(boolean active) {
        this.active = active;
    }
}