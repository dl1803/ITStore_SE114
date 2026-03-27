package com.example.itstore.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import java.util.ArrayList;
import java.util.List;
import com.example.itstore.R;
import com.example.itstore.model.Category;
import com.example.itstore.model.MockDataRepository;
import com.example.itstore.model.Product;
import com.example.itstore.model.ProductImage;
import com.example.itstore.model.ProductVariant;

public class HomeViewModel extends ViewModel{
    private final MutableLiveData<List<Category>> categoryListLiveData = new MutableLiveData<>();
    private final MutableLiveData<List<Product>> productListLiveData = new MutableLiveData<>();
    private List<Product> allProductsBackup = new ArrayList<>();
    public HomeViewModel () {
        allProductsBackup = MockDataRepository.getInstance().getAllProducts();
        List<Category> allCategories = MockDataRepository.getInstance().getAllCategories();
        categoryListLiveData.setValue(MockDataRepository.getInstance().getAllCategories());
        productListLiveData.setValue(MockDataRepository.getInstance().getAllProducts());
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

}
