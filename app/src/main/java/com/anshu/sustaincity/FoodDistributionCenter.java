package com.anshu.sustaincity;

public class FoodDistributionCenter {
    private String id;
    private String name;
    private String address;
    private String latitude;
    private String longitude;

    public FoodDistributionCenter() {} // Empty constructor for Firestore

    public FoodDistributionCenter(String id, String name, String address, String latitude, String longitude) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getAddress() { return address; }
    public String getLatitude() { return latitude; }
    public String getLongitude() { return longitude; }
}
