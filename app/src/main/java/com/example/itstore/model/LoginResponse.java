package com.example.itstore.model;

import com.google.gson.annotations.SerializedName;

public class LoginResponse {
    private User user;
    @SerializedName("access_token")
    private String accessToken;
    @SerializedName("refresh_token")
    private String refreshToken;


    public User getUser() { return user; }
    public String getAccessToken() { return accessToken; }

    public String getRefreshToken() { return refreshToken; }

    public static class User {
        private int id;
        @SerializedName("full_name")
        private String fullName;
        private String email;

        private String phone_number;
        private String role;

        public int getId() { return id; }
        public String getFullName() { return fullName; }
        public String getEmail() { return email; }
        public String getRole() { return role; }



    }
}