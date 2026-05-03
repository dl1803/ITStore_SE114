package com.example.itstore.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class Order implements Serializable {
    @SerializedName("id")
    private int id;

    @SerializedName("total")
    private double total;

    @SerializedName("order_status")
    private String orderStatus;

    @SerializedName("created_at")
    private String createdAt;

    @SerializedName("items")
    private List<OrderItem> items;
    public String getOrderId() { return String.valueOf(id); }

    public String getStatus() { return orderStatus; }

    public long getTotalPrice() { return (long) total; }

    public String getOrderDate() { return createdAt; }

    // Auto lấy tên của sản phẩm đầu trong đơn hàng
    public String getProductName() {
        if (items != null && !items.isEmpty()) {
            return items.get(0).getProductName();
        }
        return "Đơn hàng rỗng";
    }

    // Auto lấy loại sản phẩm đầu trong đơn hàng
    public String getProductType() {
        if (items != null && !items.isEmpty()) {
            return items.get(0).getProductType();
        }
        return "";
    }

    // Auto tính số lượng món phụ (Tổng số món - 1)
    public int getExtraItemsCount() {
        if (items != null && items.size() > 1) {
            return items.size() - 1;
        }
        return 0;
    }

    // Auto đếm tổng số lượng sản phẩm trong đơn hàng
    public int getQuantity() {
        int totalQty = 0;
        if (items != null) {
            for (OrderItem item : items) {
                totalQty += item.getQuantity();
            }
        }
        return totalQty;
    }

    // Auto lấy ảnh của sản phẩm đầu trong đơn hàng
    public String getImageUrl() {
        if (items != null && !items.isEmpty()) {
            return items.get(0).getImageUrl();
        }
        return "";
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public void setStatus(String newStatus) {
     this.orderStatus = newStatus;
    }
}