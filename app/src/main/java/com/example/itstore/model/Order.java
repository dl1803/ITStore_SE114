package com.example.itstore.model;

public class Order {
    private String orderId;
    private String status;
    private String productName;
    private String productType;
    private int quantity;
    private int extraItemsCount;
    private long totalPrice;

    public Order(String orderId, String status, String productName, String productType, int quantity, int extraItemsCount, long totalPrice){
        this.orderId = orderId;
        this.status = status;
        this.productName = productName;
        this.productType = productType;
        this.quantity = quantity;
        this.extraItemsCount = extraItemsCount;
        this.totalPrice = totalPrice;
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
}
