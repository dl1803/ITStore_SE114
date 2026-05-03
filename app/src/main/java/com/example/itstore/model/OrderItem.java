package com.example.itstore.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class OrderItem implements Serializable {
    @SerializedName("variant_id")
    private int variantId;
    @SerializedName("quantity")
    private int quantity;
    @SerializedName("unit_price")
    private double unitPrice;
    @SerializedName("image_url")
    private String imageUrl;

    @SerializedName("product")
    private ProductInfo productInfo;

    @SerializedName("variant")
    private VariantInfo variantInfo;

    public int getQuantity() { return quantity; }

    public double getPrice() { return unitPrice; }

    public String getImageUrl() { return imageUrl; }

    public String getProductName() {
        return productInfo != null ? productInfo.name : "Sản phẩm không rõ";
    }

    public String getProductType() {
        if (variantInfo != null) {
            String type = "";
            if (variantInfo.version != null && !variantInfo.version.isEmpty()) {
                type += variantInfo.version;
            }
            if (variantInfo.color != null && !variantInfo.color.isEmpty()) {
                type += (type.isEmpty() ? "" : " - ") + variantInfo.color;
            }
            return type.isEmpty() ? "Mặc định" : type;
        }
        return "";
    }

    public int getProductId() {
        return productInfo != null ? productInfo.id : -1;
    }

    public static class ProductInfo implements Serializable {

        @SerializedName("id")
        private int id;
        @SerializedName("name")
        private String name;
    }

    public static class VariantInfo implements Serializable {
        @SerializedName("version")
        private String version;
        @SerializedName("color")
        private String color;
    }
}