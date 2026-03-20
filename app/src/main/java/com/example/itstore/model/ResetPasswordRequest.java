package com.example.itstore.model;

public class ResetPasswordRequest {
    private String token;
    private String newPassword;

    public ResetPasswordRequest(String token, String newPassword) {
        this.token = token;
        this.newPassword = newPassword;
    }

}
