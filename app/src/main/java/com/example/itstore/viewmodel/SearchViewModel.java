package com.example.itstore.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.itstore.model.Category;
import com.example.itstore.model.MockDataRepository;
import com.example.itstore.model.Product;

import java.util.ArrayList;
import java.util.List;

public class SearchViewModel extends ViewModel {
    private MutableLiveData<List<Product>> _searchResults = new MutableLiveData<>();
    public LiveData<List<Product>> searchResults = _searchResults;
    private List<Product> allProducts;
    private List<Category> allCategories;
    public SearchViewModel() {
        allProducts = MockDataRepository.getInstance().getAllProducts();
        allCategories = MockDataRepository.getInstance().getAllCategories();
        filterProducts("", "Tất cả");
    }
    public void filterProducts(String query, String categoryName) {
        List<Product> filteredList = new ArrayList<>();
        int targetCategoryId = -1;
        if (categoryName != null && !categoryName.equalsIgnoreCase("Tất cả")) {
            for (Category cat : allCategories) {
                if (cat.getName().equalsIgnoreCase(categoryName)) {
                    targetCategoryId = cat.getId();
                    break;
                }
            }
        }
        for (Product p : allProducts) {
            boolean matchesQuery = query.isEmpty() ||
                    (p.getName() != null && p.getName().toLowerCase().contains(query.toLowerCase())) ||
                    (p.getDescription() != null && p.getDescription().toLowerCase().contains(query.toLowerCase()));
            boolean matchesCategory = (targetCategoryId == -1) || (p.getCategoryId() == targetCategoryId);

            if (matchesQuery && matchesCategory) {
                filteredList.add(p);
            }
        }
        _searchResults.setValue(filteredList);
    }
}
