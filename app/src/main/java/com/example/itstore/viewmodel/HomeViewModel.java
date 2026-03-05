package com.example.itstore.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import java.util.ArrayList;
import java.util.List;
import com.example.itstore.R;
import com.example.itstore.model.Category;
import com.example.itstore.model.Product;
public class HomeViewModel extends ViewModel{
    private final MutableLiveData<List<Category>> categoryListLiveData = new MutableLiveData<>();
    private final MutableLiveData<List<Product>> productListLiveData = new MutableLiveData<>();
    public HomeViewModel () {
        loadMockData();
    }

    public MutableLiveData<List<Category>> getCategoryListLiveData() {
        return categoryListLiveData;
    }

    public MutableLiveData<List<Product>> getProductListLiveData() {
        return productListLiveData;
    }
    private void loadMockData () {
        String mockImageUrl = "android.resource://com.example.itstore/" + R.drawable.ram1;
        List<Category> mockCategories = new ArrayList<>();
        mockCategories.add(new Category(1, "CPU", mockImageUrl));
        mockCategories.add(new Category(2, "RAM", mockImageUrl));
        mockCategories.add(new Category(3, "VGA", mockImageUrl));
        categoryListLiveData.setValue(mockCategories);
        List<Product> mockProducts = new ArrayList<>();
        mockProducts.add(new Product(101, 1, "CPU Intel Core i9-14900K", 15000000.0, mockImageUrl, "CPU mạnh nhất của Intel, 24 nhân 32 luồng siêu mượt.", 50));
        mockProducts.add(new Product(102, 2, "RAM Corsair Vengeance 32GB", 2500000.0, mockImageUrl, "RAM DDR5 tốc độ cao 6000MHz có LED RGB.", 150));
        mockProducts.add(new Product(103, 3, "VGA NVIDIA RTX 4090", 50000000.0, mockImageUrl, "Card đồ họa khủng nhất hiện nay.", 10));
        mockProducts.add(new Product(104, 1, "CPU AMD Ryzen 9 7950X", 14000000.0, mockImageUrl, "Đối thủ truyền kiếp của Core i9.", 30));
        mockProducts.add(new Product(105, 2, "RAM Kingston Fury 16GB", 1200000.0, mockImageUrl, "RAM DDR4 giá rẻ hiệu năng cao.", 200));
        productListLiveData.setValue(mockProducts);
    }
}
