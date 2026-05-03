package com.example.itstore.viewmodel;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.itstore.api.RetrofitClient;
import com.example.itstore.model.Brand;
import com.example.itstore.model.BrandResponse;
import com.example.itstore.model.Category;
import com.example.itstore.model.MockDataRepository;
import com.example.itstore.model.Product;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchViewModel extends ViewModel {
    private MutableLiveData<List<Product>> _searchResults = new MutableLiveData<>();
    public LiveData<List<Product>> searchResults = _searchResults;
    private List<Product> allProducts;
    private List<Category> allCategories;
    private MutableLiveData<List<Brand>> listBrandsLiveData = new MutableLiveData<>();

    public MutableLiveData<List<Brand>> getListBrandsLiveData() {
        return listBrandsLiveData;
    }

    public SearchViewModel() {
        allProducts = MockDataRepository.getInstance().getAllProducts();
        filterProducts("", -1, 0, Double.MAX_VALUE, new ArrayList<Integer>());
    }
    public void filterProducts(String query, int targetCategoryId, double minPrice, double maxPrice, List<Integer> selectedBrandIds) {
        List<Product> filteredList = new ArrayList<>();

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
            boolean matchesBrand = true;
            if (selectedBrandIds != null && !selectedBrandIds.isEmpty()) {
                matchesBrand = selectedBrandIds.contains(p.getBrandId());
            }

            // Vượt qua cả 4 vòng thì mới được hiển thị
            if (matchesQuery && matchesCategory && matchesPrice && matchesBrand) {
                filteredList.add(p);
            }
        }
        _searchResults.setValue(filteredList);
    }
    public void fetchBrands(Context context) {
        RetrofitClient.ApiService apiService = RetrofitClient.getApiService(context);

        apiService.getBrands().enqueue(new Callback<BrandResponse>() {
            @Override
            public void onResponse(Call<BrandResponse> call, Response<BrandResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().isSuccess()) {
                        listBrandsLiveData.setValue(response.body().getData());
                    }
                }
            }
            @Override
            public void onFailure(Call<BrandResponse> call, Throwable t) {
                android.util.Log.e("API_ERR", "Lỗi lấy Brand bên Search: " + t.getMessage());
            }
        });
    }
}