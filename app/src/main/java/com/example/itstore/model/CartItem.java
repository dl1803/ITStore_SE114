package com.example.itstore.model;

import com.google.gson.annotations.SerializedName;

public class CartItem {
    private int id;
    @SerializedName("variant_id")
    private int variantId;
    private int quantity;
    private double price;
    @SerializedName("image_url")
    private String imageUrl;
    @SerializedName("product")
    private Product product;
    @SerializedName("variant")
    private CartVariantInfo variantInfo;
    private boolean isSelected = false;

    public static class CartVariantInfo {
        private String version;
        private String color;

        public String getVersion() { return version; }
        public String getColor() { return color; }
    }

    public CartItem() {}

    public CartItem(int id, int cartId, int variantId, int quantity, Product product, String variantName, double price) {
        this.id = id;
        this.variantId = variantId;
        this.quantity = quantity;
        this.product = product;
        this.price = price;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getVariantId() { return variantId; }
    public void setVariantId(int variantId) { this.variantId = variantId; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public String getImageUrl() { return imageUrl; }

    public Product getProduct() { return product; }
    public void setProduct(Product product) { this.product = product; }

    public String getVariantName() {
        if (variantInfo == null) return "Mặc định";
        String version = variantInfo.getVersion();
        String color = variantInfo.getColor();
        StringBuilder sb = new StringBuilder();
        if (version != null && !version.isEmpty()) sb.append(version);
        if (color != null && !color.isEmpty()) {
            if (sb.length() > 0) sb.append(" - ");
            sb.append(color);
        }
        return sb.length() > 0 ? sb.toString() : "Mặc định";
    }

    public boolean isSelected() { return isSelected; }
    public void setSelected(boolean selected) { isSelected = selected; }
}