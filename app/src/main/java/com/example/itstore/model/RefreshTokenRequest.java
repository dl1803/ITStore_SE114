package com.example.itstore.model;

import com.google.gson.annotations.SerializedName;

public class RefreshTokenRequest {

    @SerializedName("refresh_token")
    private String refresh_token;

    public RefreshTokenRequest(String refresh_token) {
        this.refresh_token = refresh_token;
    }



}
