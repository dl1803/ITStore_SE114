package com.example.itstore.model;

import com.google.gson.annotations.SerializedName;

public class UpdateProfileRequest {
    @SerializedName("full_name")
    private String fullName;

    @SerializedName("phone_number")
    private String phoneNumber;

    public UpdateProfileRequest(String fullName, String phoneNumber) {
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
    }

}