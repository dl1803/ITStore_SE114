package com.example.itstore.model;
import com.google.gson.annotations.SerializedName;

public class GhnFeeData {
    @SerializedName("total")
    private double totalFee;


    public double getTotalFee() { return totalFee; }

}