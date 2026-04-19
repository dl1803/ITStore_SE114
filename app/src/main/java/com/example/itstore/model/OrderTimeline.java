package com.example.itstore.model;

public class OrderTimeline {
    private String status;
    private String time;

    public OrderTimeline(String status, String time) {
        this.status = status;
        this.time = time;
    }

    public String getStatus() { return status; }
    public String getTime() { return time; }
}