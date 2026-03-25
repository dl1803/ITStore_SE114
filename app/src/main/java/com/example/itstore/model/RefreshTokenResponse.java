package com.example.itstore.model;

import com.google.gson.annotations.SerializedName;

public class RefreshTokenResponse {

    @SerializedName("access_token")
    private String access_token;

    @SerializedName("message")
    private String message;

    public String getAccessToken() {
        return access_token;
    }


    public String getMessage() {
        return message;
    }
}
