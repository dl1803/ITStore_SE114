package com.example.itstore.model;

public class ProfileResponse {
    private boolean success;
    private User data;

    public boolean isSuccess() {
        return success;
    }

    public User getData() {
        return data;
    }
}
