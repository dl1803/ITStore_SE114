package com.example.itstore.model;

public class Banner {
    private int id;
    private String image_url;
    private String link_url;
    private int sort_order;
    private boolean is_active;
    private String end_date;

    public int getId() { return id; }
    public String getImageUrl() { return image_url; }
    public String getLinkUrl() { return link_url; }
    public int getSortOrder() { return sort_order; }
    public boolean isActive() { return is_active; }
    public String getEndDate() { return end_date; }
}