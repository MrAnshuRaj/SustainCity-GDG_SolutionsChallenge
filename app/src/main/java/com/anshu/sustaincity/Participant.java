package com.anshu.sustaincity;

public class Participant {
    private String userId;
    private String joinedAt;

    public Participant() {
        // Firestore requires a no-arg constructor
    }

    public Participant(String userId) {
        this.userId = userId;
        this.joinedAt = String.valueOf(System.currentTimeMillis()); // Store timestamp
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getJoinedAt() {
        return joinedAt;
    }

    public void setJoinedAt(String joinedAt) {
        this.joinedAt = joinedAt;
    }
}
