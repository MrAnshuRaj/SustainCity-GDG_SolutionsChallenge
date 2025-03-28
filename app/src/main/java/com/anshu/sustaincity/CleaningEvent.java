package com.anshu.sustaincity;

public class CleaningEvent {
    private String id;
    private String title;
    private String description;
    private String date;
   private String location;

    public CleaningEvent() {} // Required for Firestore

    public CleaningEvent(String title, String description, String date, String location) {
        this.title = title;
        this.description = description;
        this.date = date;
        this.location = location;
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getDate() { return date; }
   public String getLocation() { return location; }
}
