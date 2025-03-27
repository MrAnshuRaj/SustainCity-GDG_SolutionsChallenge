package com.anshu.sustaincity;

public class Bike {
    private String name, type, price, imageUrl, ownerContact;

    public Bike() {
        // Required empty constructor for Firebase
    }

    public Bike(String name, String type, String price, String imageUrl, String ownerContact) {
        this.name = name;
        this.type = type;
        this.price = price;
        this.imageUrl = imageUrl;
        this.ownerContact = ownerContact;
    }

    public String getName() { return name; }
    public String getType() { return type; }
    public String getPrice() { return price; }
    public String getImageUrl() { return imageUrl; }
    public String getOwnerContact() { return ownerContact; }
}
