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

public class HomeViewModel extends ViewModel {
    private final MutableLiveData<List<Category>> categoryListLiveData = new MutableLiveData<>();
    private final MutableLiveData<List<Product>> productListLiveData = new MutableLiveData<>();
    private List<Product> allProducts = new ArrayList<>();
    public HomeViewModel() {
        allProducts = MockDataRepository.getInstance().getAllProducts();
        categoryListLiveData.setValue(MockDataRepository.getInstance().getAllCategories());
        productListLiveData.setValue(new ArrayList<>(allProducts));
    }
    public MutableLiveData<List<Category>> getCategoryListLiveData() {
        return categoryListLiveData;
    }
    public MutableLiveData<List<Product>> getProductListLiveData() {
        return productListLiveData;
    }
    public void updateProduct(Product updatedProduct) {
        for (int i = 0; i < allProducts.size(); i++) {
            if (allProducts.get(i).getId() == updatedProduct.getId()) {
                allProducts.set(i, updatedProduct);
                break;
            }
        }
        List<Product> currentDisplayList = productListLiveData.getValue();
        if (currentDisplayList != null) {
            for (int i = 0; i < currentDisplayList.size(); i++) {
                if (currentDisplayList.get(i).getId() == updatedProduct.getId()) {
                    currentDisplayList.set(i, updatedProduct);
                    break;
                }
            }
            productListLiveData.setValue(new ArrayList<>(currentDisplayList));
        }
    }

    public void filterByCategory(int categoryId) {
        if (categoryId == -1) {
            productListLiveData.setValue(new ArrayList<>(allProducts));
            return;
        }
        List<Product> filteredList = new ArrayList<>();
        for (Product item : allProducts) {
            if (item.getCategoryId() == categoryId) {
                filteredList.add(item);
            }
        }
        productListLiveData.setValue(filteredList);
    }
}


