package com.example.itstore.model;
import java.io.Serializable;
public class ProductVariant implements Serializable{
    private int id;
    private String version;
    private double price;
    private double compareAtPrice;
    private int stock;



    public ProductVariant(int id, String version, double price, double compareAtPrice, int stock) {
        this.id = id;
        this.version = version;
        this.price = price;
        this.compareAtPrice = compareAtPrice;
        this.stock = stock;
    }

    public int getId() {
        return id;
    }

    public String getVersion() {
        return version;
    }

    public double getPrice() {
        return price;
    }

    public double getCompareAtPrice() {
        return compareAtPrice;
    }

    public int getStock() {
        return stock;
    }
}
