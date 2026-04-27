package com.example.itstore.model;

import com.google.gson.annotations.SerializedName;

public class Brand {
    private int id;
    private String name;
    @SerializedName("logo_url")
    private String logoUrl;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getLogoUrl() {
        return logoUrl;
    }
}
