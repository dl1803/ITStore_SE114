package com.example.itstore.model;

public class RefreshTokenResponse {
    private String access_token;
    private String refresh_token;

    private String message;

    public String getAccessToken() {
        return access_token;
    }

    public String getRefreshToken() {
        return refresh_token;
    }

    public String getMessage() {
        return message;
    }
}
