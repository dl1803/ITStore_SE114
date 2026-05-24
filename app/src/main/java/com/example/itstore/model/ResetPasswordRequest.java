package com.example.itstore.model;

import com.google.gson.annotations.SerializedName;

public class ResetPasswordRequest {
    @SerializedName("new_password")
    private String newPassword;
    public ResetPasswordRequest(String new_password) { this.newPassword = new_password; }

}
