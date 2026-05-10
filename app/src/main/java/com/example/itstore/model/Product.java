package com.example.itstore.model;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Product implements Serializable {
    private int id;
    private int categoryId;
    private String name;
    private String description;
    private List<ProductVariant> variants;
    private List<ProductImage> images;
    private boolean isFavorite = false;
    private int quantity;
    private int brandId;
    @SerializedName("primary_image")
    private String primaryImage;

    @SerializedName("price_min")
    private double priceMin;

    @SerializedName("price_max")
    private double priceMax;
    @SerializedName("category")
    private Category categoryObj;
    @SerializedName("slug")
    private String slug;
    @SerializedName("specifications")
    private Map<String, String> specificationsMap;

    public Product(int id, int categoryId, String name, String description, List<ProductVariant> variants, List<ProductImage> images, int brandId) {
        this.id = id;
        this.categoryId = categoryId;
        this.name = name;
        this.description = description;
        this.variants = variants;
        this.images = images;
        this.brandId = brandId;
    }

    public int getId() {
        return id;
    }

    public int getCategoryId() {
        if (categoryObj != null) {
            return categoryObj.getId();
        }
        return categoryId;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public List<ProductVariant> getVariants() {
        return variants;
    }

    public List<ProductImage> getImages() {
        return images;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getBrandId() {
        return brandId;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setVariants(List<ProductVariant> variants) {
        this.variants = variants;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setImages(List<ProductImage> images) {
        this.images = images;
    }

    public void setBrandId(int brandId) {
        this.brandId = brandId;
    }

    public String getPrimaryImage() {
        return primaryImage;
    }

    public void setPrimaryImage(String primaryImage) {
        this.primaryImage = primaryImage;
    }

    public double getPriceMin() {
        return priceMin;
    }

    public void setPriceMin(double priceMin) {
        this.priceMin = priceMin;
    }

    public double getPriceMax() {
        return priceMax;
    }

    public void setPriceMax(double priceMax) {
        this.priceMax = priceMax;
    }

    public double getPrice() {
        if (priceMin > 0) {
            return priceMin;
        }
        if (variants != null && !variants.isEmpty()) {
            return variants.get(0).getPrice();
        }
        return 0;
    }
    public double getCompareAtPrice() {
        if (variants != null && !variants.isEmpty()) {
            return variants.get(0).getCompareAtPrice();
        }
        return 0;
    }
    public String getImageUrl() {
        if (primaryImage != null && !primaryImage.isEmpty()) {
            return primaryImage;
        }
        if (images != null && !images.isEmpty()) {
            for (ProductImage img : images) {
                if (img.isPrimary()) return img.getImageUrl();
            }
            return images.get(0).getImageUrl();
        }
        return "";
    }

    public String getSlug() {
        return slug;
    }
    public List<Specification> getSpecificationList() {
        List<Specification> list = new ArrayList<>();
        if (specificationsMap != null) {
            for (Map.Entry<String, String> entry : specificationsMap.entrySet()) {
                list.add(new Specification(entry.getKey(), entry.getValue()));
            }
        }
        return list;
    }
}