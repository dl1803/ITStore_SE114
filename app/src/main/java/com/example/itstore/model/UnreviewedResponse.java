package com.example.itstore.model;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;
import java.util.List;

public class UnreviewedResponse implements Serializable {
    private boolean success;
    private List<UnreviewedItem> data;

    public boolean isSuccess() { return success; }
    public List<UnreviewedItem> getData() { return data; }

    public static class UnreviewedItem implements Serializable {
        private int order_item_id;
        private int remaining_days;
        private String image_url;

        private ProductInfo product;

        private VariantInfo variant;

        public int getOrderItemId() { return order_item_id; }
        public int getRemainingDays() { return remaining_days; }
        public String getProductName() {
            return (product != null) ? product.name : "";
        }
        public String getProductImage() {
            return image_url;
        }
        public String getVersion() {
            if (variant != null) {
                return variant.color + " - Phiên bản " + variant.version;
            }
            return "Tiêu chuẩn";
        }
    }

    public static class ProductInfo implements Serializable {
        private int id;
        private String name;
        private String slug;
    }
    public static class VariantInfo implements Serializable {
        private int id;
        private String sku;
        private String version;
        private String color;
    }
}