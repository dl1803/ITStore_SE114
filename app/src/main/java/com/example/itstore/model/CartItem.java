package com.example.itstore.model;

public class CartItem {
    private int id;
    private int cartId;
    private int variantId;
    private int quantity;

    private Product product;
    private String variantName;
    private double price;
    private boolean isSelected = false;

    public CartItem(int id, int cartId, int variantId, int quantity, Product product, String variantName, double price) {
        this.id = id;
        this.cartId = cartId;
        this.variantId = variantId;
        this.quantity = quantity;
        this.product = product;
        this.variantName = variantName;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCartId() {
        return cartId;
    }

    public void setCartId(int cartId) {
        this.cartId = cartId;
    }

    public int getVariantId() {
        return variantId;
    }

    public void setVariantId(int variantId) {
        this.variantId = variantId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public String getVariantName() {
        return variantName;
    }

    public void setVariantName(String variantName) {
        this.variantName = variantName;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
