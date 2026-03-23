package com.example.itstore.model;

import com.google.gson.annotations.SerializedName;

public class ResetPasswordRequest {
    private String token;

    @SerializedName("new_password")
    private String newPassword;

    public ResetPasswordRequest(String token, String newPassword) {
        this.token = token;
        this.newPassword = newPassword;
    }

}
