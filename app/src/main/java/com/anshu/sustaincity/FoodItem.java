package com.anshu.sustaincity;

public class FoodItem {
    private String id;
    private String name;
    private String quantity;
    private String expiryDate;

    public FoodItem() {} // Empty constructor for Firestore

    public FoodItem(String id, String name, String quantity, String expiryDate) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.expiryDate = expiryDate;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getQuantity() { return quantity; }
    public String getExpiryDate() { return expiryDate; }
}
