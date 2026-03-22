package com.example.itstore.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import java.util.ArrayList;
import java.util.List;
import com.example.itstore.R;
import com.example.itstore.model.Category;
import com.example.itstore.model.Product;
import com.example.itstore.model.ProductImage;
import com.example.itstore.model.ProductVariant;

public class HomeViewModel extends ViewModel{
    private final MutableLiveData<List<Category>> categoryListLiveData = new MutableLiveData<>();
    private final MutableLiveData<List<Product>> productListLiveData = new MutableLiveData<>();
    private List<Product> allProductsBackup = new ArrayList<>();
    public HomeViewModel () {
        loadMockData();
    }

    public MutableLiveData<List<Category>> getCategoryListLiveData() {
        return categoryListLiveData;
    }

    public MutableLiveData<List<Product>> getProductListLiveData() {
        return productListLiveData;
    }
    public void filterByCategory(int categoryId) {
        if (categoryId == -1) {
            productListLiveData.setValue(allProductsBackup);
            return;
        }
        List<Product> filteredList = new ArrayList<>();
        for (Product item : allProductsBackup) {
            if (item.getCategoryId() == categoryId) {
                filteredList.add(item);
            }
        }
        productListLiveData.setValue(filteredList);
    }
    private void loadMockData() {
        String mockImageUrl = "android.resource://com.example.itstore/" + R.drawable.ram1;
        List<Category> mockCategories = new ArrayList<>();
        mockCategories.add(new Category(-1, "Tất cả", mockImageUrl));
        mockCategories.add(new Category(1, "CPU", mockImageUrl));
        mockCategories.add(new Category(2, "RAM", mockImageUrl));
        mockCategories.add(new Category(3, "VGA", mockImageUrl));
        categoryListLiveData.setValue(mockCategories);
        List<Product> mockProducts = new ArrayList<>();
        List<ProductImage> defaultImages = new ArrayList<>();
        defaultImages.add(new ProductImage(1, mockImageUrl, true));
        List<ProductVariant> var1 = new ArrayList<>();
        var1.add(new ProductVariant(1, "Mặc định", 15000000.0, 18000000.0, 50));
        mockProducts.add(new Product(101, 1, "CPU Intel Core i9-14900K", "CPU mạnh nhất của Intel, 24 nhân 32 luồng siêu mượt.", var1, defaultImages));
        List<ProductVariant> var2 = new ArrayList<>();
        var2.add(new ProductVariant(2, "32GB", 2500000.0, 2800000.0, 150));
        mockProducts.add(new Product(102, 2, "RAM Corsair Vengeance 32GB", "RAM DDR5 tốc độ cao 6000MHz có LED RGB.", var2, defaultImages));
        List<ProductVariant> var3 = new ArrayList<>();
        var3.add(new ProductVariant(3, "24GB", 50000000.0, 55000000.0, 10));
        mockProducts.add(new Product(103, 3, "VGA NVIDIA RTX 4090", "Card đồ họa khủng nhất hiện nay.", var3, defaultImages));
        List<ProductVariant> var4 = new ArrayList<>();
        var4.add(new ProductVariant(4, "Mặc định", 14000000.0, 18000000.0, 30));
        mockProducts.add(new Product(104, 1, "CPU AMD Ryzen 9 7950X", "Đối thủ truyền kiếp của Core i9.", var4, defaultImages));
        List<ProductVariant> var5 = new ArrayList<>();
        var5.add(new ProductVariant(5, "16GB", 1200000.0, 1500000.0, 200));
        mockProducts.add(new Product(105, 2, "RAM Kingston Fury 16GB", "RAM DDR4 giá rẻ hiệu năng cao.", var5, defaultImages));
        allProductsBackup = new ArrayList<>(mockProducts);
        productListLiveData.setValue(mockProducts);
    }
}
