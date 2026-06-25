package com.example.mobileapp.model;

public class CheckResponse {
    private boolean exists;

    // Tra ve true neu da bi trung, false neu chua ai dung
    public boolean isExists() {
        return exists;
    }

    public void setExists(boolean exists) {
        this.exists = exists;
    }
}