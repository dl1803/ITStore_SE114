package com.example.itstore.viewmodel;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.itstore.api.RetrofitClient;
import com.example.itstore.model.Brand;
import com.example.itstore.model.BrandResponse;
import com.example.itstore.model.Category;
import com.example.itstore.model.MockDataRepository;
import com.example.itstore.model.Product;
import com.example.itstore.model.ProductResponse;
import com.example.itstore.repository.ProductRepository;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import com.example.itstore.repository.SearchHistoryRepository;

public class SearchViewModel extends AndroidViewModel {
    private final ProductRepository productRepository;
    private MutableLiveData<List<Product>> _searchResults = new MutableLiveData<>();
    public LiveData<List<Product>> searchResults = _searchResults;
    private List<Product> allProducts;
    private List<Category> allCategories;
    private MutableLiveData<List<Brand>> listBrandsLiveData = new MutableLiveData<>();
    private final SearchHistoryRepository historyRepository;
    private final MutableLiveData<List<String>> searchHistoryLiveData = new MutableLiveData<>();

    public LiveData<List<String>> getSearchHistoryLiveData() {
        return searchHistoryLiveData;
    }
    public MutableLiveData<List<Brand>> getListBrandsLiveData() {
        return listBrandsLiveData;
    }

    public SearchViewModel(@NonNull Application application) {
        super(application);
        productRepository = ProductRepository.getInstance(application);
        historyRepository = SearchHistoryRepository.getInstance(application);
        searchHistoryLiveData.setValue(historyRepository.getHistory());
    }
    public void searchProducts(String query, int categoryId, double minPrice, double maxPrice, List<Integer> brandIds) {

        Integer apiCategoryId = (categoryId == -1) ? null : categoryId;
        Double apiMinPrice = (minPrice <= 0) ? null : minPrice;
        Double apiMaxPrice = (maxPrice == Double.MAX_VALUE) ? null : maxPrice;
        Integer apiBrandId = (brandIds != null && !brandIds.isEmpty()) ? brandIds.get(0) : null;
        productRepository.getProducts(1, 20, query, apiCategoryId, apiBrandId, apiMinPrice, apiMaxPrice, null, new Callback<ProductResponse>() {
            @Override
            public void onResponse(Call<ProductResponse> call, Response<ProductResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    _searchResults.setValue(response.body().getData());
                }
            }

            @Override
            public void onFailure(Call<ProductResponse> call, Throwable t) {
                Log.e("API_ERR", "Lỗi tìm kiếm sản phẩm: " + t.getMessage());
            }
        });
    }
    public void fetchBrands() {
        productRepository.getBrands(new Callback<BrandResponse>() {
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
                Log.e("API_ERR", "Lỗi lấy Brand bên Search: " + t.getMessage());
            }
        });
    }
    public void saveKeyword(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) return;
        historyRepository.addKeyword(keyword.trim());
        searchHistoryLiveData.setValue(historyRepository.getHistory());
    }

    public void removeKeyword(String keyword) {
        historyRepository.removeKeyword(keyword);
        searchHistoryLiveData.setValue(historyRepository.getHistory());
    }

    public void clearHistory() {
        historyRepository.clearAll();
        searchHistoryLiveData.setValue(historyRepository.getHistory());
    }
}