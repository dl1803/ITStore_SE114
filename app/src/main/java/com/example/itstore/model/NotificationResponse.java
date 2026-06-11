package com.example.itstore.model;

import com.google.gson.annotations.SerializedName;

public class NotificationResponse {
    @SerializedName("success")
    private boolean success;

    @SerializedName("message")
    private String message;

    public boolean isSuccess() { return success; }
    public String getMessage() { return message; }
}