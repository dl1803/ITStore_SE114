package com.example.itstore.model;

public class ChangePasswordResponse {
    private Boolean success;
    private String message;

    public ChangePasswordResponse(Boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public Boolean getSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }
}
