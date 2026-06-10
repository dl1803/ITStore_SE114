package com.example.itstore.model;

import com.google.gson.annotations.SerializedName;

public class NotificationUnreadCountResponse {
    @SerializedName("success")
    private boolean success;

    @SerializedName("count")
    private int count;

    public boolean isSuccess() { return success; }
    public int getCount() { return count; }
}