package com.example.itstore.model;
import com.google.gson.annotations.SerializedName;

public class GhnWard {
    @SerializedName("WardCode")
    private String wardCode;

    @SerializedName("WardName")
    private String wardName;

    public String getWardCode() { return wardCode; }
    public String getWardName() { return wardName; }

    @Override
    public String toString() { return wardName; }
}