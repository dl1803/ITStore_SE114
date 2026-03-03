package com.example.itstore.model;
import com.google.gson.annotations.SerializedName;
import java.io.Serializable;
public class Product implements Serializable {
    @SerializedName("id")
    private int id;
    @SerializedName("category_id")
    private int categoryId;
    @SerializedName("name")
    private String name;
    @SerializedName("price")
    private double price;
    @SerializedName("image_url")
    private String imageUrl;
    @SerializedName("description")
    private String description;
    @SerializedName("stock")
    private int stock;

    public Product() {
    }

    public Product(int id, int categoryId, String name, double price, String imageUrl, String description, int stock) {
        this.id = id;
        this.categoryId = categoryId;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.description = description;
        this.stock = stock;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }
}

