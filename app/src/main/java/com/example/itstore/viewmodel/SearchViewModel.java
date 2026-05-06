package com.example.itstore.viewmodel;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.itstore.api.RetrofitClient;
import com.example.itstore.model.Brand;
import com.example.itstore.model.BrandResponse;
import com.example.itstore.model.Category;
import com.example.itstore.model.MockDataRepository;
import com.example.itstore.model.Product;
import com.example.itstore.model.ProductResponse;

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
    }
    public void searchProducts(Context context, String query, int categoryId, double minPrice, double maxPrice, List<Integer> brandIds) {

        Integer apiCategoryId = (categoryId == -1) ? null : categoryId;
        Double apiMinPrice = (minPrice <= 0) ? null : minPrice;
        Double apiMaxPrice = (maxPrice == Double.MAX_VALUE) ? null : maxPrice;

        Integer apiBrandId = (brandIds != null && !brandIds.isEmpty()) ? brandIds.get(0) : null;
        RetrofitClient.getApiService(context).getProducts(
                1, 20, query, apiCategoryId, apiBrandId, apiMinPrice, apiMaxPrice, null
        ).enqueue(new Callback<ProductResponse>() {
            @Override
            public void onResponse(Call<ProductResponse> call, Response<ProductResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    _searchResults.setValue(response.body().getData());
                }
            }

            @Override
            public void onFailure(Call<ProductResponse> call, Throwable t) {
                android.util.Log.e("API_ERR", "Lỗi tìm kiếm sản phẩm: " + t.getMessage());
            }
        });
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
                Log.e("API_ERR", "Lỗi lấy Brand bên Search: " + t.getMessage());
            }
        });
    }
}