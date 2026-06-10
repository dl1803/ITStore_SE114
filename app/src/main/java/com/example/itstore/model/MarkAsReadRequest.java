package com.example.itstore.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class MarkAsReadRequest {
    @SerializedName("notificationIds")
    private List<Integer> notificationIds;

    public MarkAsReadRequest(List<Integer> notificationIds) {
        this.notificationIds = notificationIds;
    }
}