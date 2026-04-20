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
        filterProducts("", "Tất cả", 0, Double.MAX_VALUE, new ArrayList<>());
    }
    public void filterProducts(String query, String categoryName, double minPrice, double maxPrice, List<String> selectedBrands) {
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
            // 1. Lọc từ khóa
            boolean matchesQuery = query.isEmpty() ||
                    (p.getName() != null && p.getName().toLowerCase().contains(query.toLowerCase())) ||
                    (p.getDescription() != null && p.getDescription().toLowerCase().contains(query.toLowerCase()));

            // 2. Lọc danh mục (Chip)
            boolean matchesCategory = (targetCategoryId == -1) || (p.getCategoryId() == targetCategoryId);

            // 3. Lọc Giá tiền (Dialog)
            double currentPrice = p.getPrice();
            boolean matchesPrice = currentPrice >= minPrice && currentPrice <= maxPrice;

            // 4. Lọc Thương hiệu (Dialog)
            boolean matchesBrand = selectedBrands == null || selectedBrands.isEmpty();

            // Vượt qua cả 4 vòng thì mới được hiển thị
            if (matchesQuery && matchesCategory && matchesPrice && matchesBrand) {
                filteredList.add(p);
            }
        }
        _searchResults.setValue(filteredList);
    }
}