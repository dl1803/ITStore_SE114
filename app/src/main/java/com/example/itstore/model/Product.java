package com.example.itstore.model;
import java.io.Serializable;
import java.util.List;

public class Product implements Serializable {
    private int id;
    private int categoryId;
    private String name;
    private String description;
    private List<ProductVariant> variants;
    private List<ProductImage> images;

    public Product(int id, int categoryId, String name, String description, List<ProductVariant> variants, List<ProductImage> images) {
        this.id = id;
        this.categoryId = categoryId;
        this.name = name;
        this.description = description;
        this.variants = variants;
        this.images = images;
    }

    public int getId() {
        return id;
    }

    public int getCategoryId() {
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

    public double getPrice() {
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
        if (images != null && !images.isEmpty()) {
            for (ProductImage img : images) {
                if (img.isPrimary()) return img.getImageUrl();
            }
            return images.get(0).getImageUrl();
        }
        return "";
    }
}