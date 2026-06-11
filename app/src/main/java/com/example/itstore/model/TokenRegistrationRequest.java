package com.example.itstore.model;

import com.google.gson.annotations.SerializedName;

public class TokenRegistrationRequest {
    @SerializedName("token")
    private String token;

    @SerializedName("device_type")
    private String deviceType = "android";

    public TokenRegistrationRequest(String token) {
        this.token = token;
    }
}