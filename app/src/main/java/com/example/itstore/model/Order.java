package com.example.itstore.model;

import java.io.Serializable;

public class Order implements Serializable {
    private String orderId;
    private String status;
    private String productName;
    private String productType;
    private int quantity;
    private int extraItemsCount;
    private long totalPrice;
    private int imageRes;
    public Order(String orderId, String status, String productName, String productType, int quantity, int extraItemsCount, long totalPrice, int imageRes){
        this.orderId = orderId;
        this.status = status;
        this.productName = productName;
        this.productType = productType;
        this.quantity = quantity;
        this.extraItemsCount = extraItemsCount;
        this.totalPrice = totalPrice;
        this.imageRes = imageRes;
    }

    public String getOrderId(){
        return orderId;
    }

    public String getStatus(){
        return status;
    }

    public String getProductName(){
        return productName;
    }

    public String getProductType(){
        return productType;
    }

    public int getQuantity(){
        return quantity;
    }

    public int getExtraItemsCount(){
        return extraItemsCount;
    }

    public long getTotalPrice(){
        return totalPrice;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getImageRes(){
        return imageRes;
    }
}
