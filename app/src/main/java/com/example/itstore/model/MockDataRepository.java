package com.example.itstore.model;

import com.example.itstore.R;

import java.util.ArrayList;
import java.util.List;

public class MockDataRepository {
    private static MockDataRepository instance;
    private List<Product> allProducts;
    private List<Category> allCategories;
    public static MockDataRepository getInstance() {
        if (instance == null) {
            instance = new MockDataRepository();
        }
        return instance;
    }
    private MockDataRepository() {
        allProducts = new ArrayList<>();
        allCategories = new ArrayList<>();
        generateMockData();
    }
    private void generateMockData(){
        String mockImageUrl = "android.resource://com.example.itstore/" + R.drawable.ram1;
        allCategories.add(new Category(-1, "Tất cả", mockImageUrl));
        allCategories.add(new Category(1, "CPU", mockImageUrl));
        allCategories.add(new Category(2, "RAM", mockImageUrl));
        allCategories.add(new Category(3, "VGA", mockImageUrl));
        List<ProductImage> defaultImages = new ArrayList<>();
        defaultImages.add(new ProductImage(1, mockImageUrl, true));
        List<ProductVariant> var1 = new ArrayList<>();
        var1.add(new ProductVariant(1, "Mặc định", 15000000.0, 18000000.0, 50));
        allProducts.add(new Product(101, 1, "CPU Intel Core i9-14900K", "CPU mạnh nhất của Intel, 24 nhân 32 luồng siêu mượt.", var1, defaultImages));
        List<ProductVariant> var2 = new ArrayList<>();
        var2.add(new ProductVariant(2, "32GB", 2500000.0, 2800000.0, 150));
        allProducts.add(new Product(102, 2, "RAM Corsair Vengeance 32GB", "RAM DDR5 tốc độ cao 6000MHz có LED RGB.", var2, defaultImages));
        List<ProductVariant> var3 = new ArrayList<>();
        var3.add(new ProductVariant(3, "24GB", 50000000.0, 55000000.0, 10));
        allProducts.add(new Product(103, 3, "VGA NVIDIA RTX 4090", "Card đồ họa khủng nhất hiện nay.", var3, defaultImages));
        List<ProductVariant> var4 = new ArrayList<>();
        var4.add(new ProductVariant(4, "Mặc định", 14000000.0, 18000000.0, 30));
        allProducts.add(new Product(104, 1, "CPU AMD Ryzen 9 7950X", "Đối thủ truyền kiếp của Core i9.", var4, defaultImages));
        List<ProductVariant> var5 = new ArrayList<>();
        var5.add(new ProductVariant(5, "16GB", 1200000.0, 1500000.0, 200));
        allProducts.add(new Product(105, 2, "RAM Kingston Fury 16GB", "RAM DDR4 giá rẻ hiệu năng cao.", var5, defaultImages));
    }
    public List<Product> getAllProducts() {
        return allProducts;
    }

    public List<Category> getAllCategories() {
        return allCategories;
    }

}
