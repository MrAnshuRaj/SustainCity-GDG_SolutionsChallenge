package com.anshu.sustaincity;

public class FoodDonation {
    private String foodId;
    private String foodName;
    private String quantity;
    private String expiryDate;
    private String imageUrl;
    private double latitude;
    private double longitude;
    private String status; // New field to track food pickup status

    // Required empty constructor for Firebase
    public FoodDonation() {
    }

    public FoodDonation(String foodId, String foodName, String quantity, String expiryDate, String imageUrl, double latitude, double longitude, String status) {
        this.foodId = foodId;
        this.foodName = foodName;
        this.quantity = quantity;
        this.expiryDate = expiryDate;
        this.imageUrl = imageUrl;
        this.latitude = latitude;
        this.longitude = longitude;
        this.status = status;
    }

    // Getters and Setters
    public String getFoodId() {
        return foodId;
    }

    public void setFoodId(String foodId) {
        this.foodId = foodId;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
