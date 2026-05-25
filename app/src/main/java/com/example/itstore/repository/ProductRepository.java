package com.example.itstore.repository;

import android.content.Context;

import com.example.itstore.api.RetrofitClient;
import com.example.itstore.model.AddCartItemRequest;
import com.example.itstore.model.CartResponse;
import com.example.itstore.model.CategoryResponse;
import com.example.itstore.model.ProductResponse;
import com.example.itstore.model.SingleProductResponse;

import retrofit2.Callback;

public class ProductRepository {
    private static ProductRepository instance;
    private final RetrofitClient.ApiService apiService;

    private ProductRepository(Context context) {
        this.apiService = RetrofitClient.getApiService(context);
    }

    public static synchronized ProductRepository getInstance(Context context) {
        if (instance == null) {
            instance = new ProductRepository(context.getApplicationContext());
        }
        return instance;
    }
    public void getProductBySlug(String slug, Callback<SingleProductResponse> callback) {
        apiService.getProductBySlug(slug).enqueue(callback);
    }
    public void getCategories(Callback<CategoryResponse> callback) {
        apiService.getCategories().enqueue(callback);
    }
    public void getProducts(Integer page, Integer limit, String keyword, Integer categoryId,
                            Integer brandId, Double priceMin, Double priceMax, String sort,
                            Callback<ProductResponse> callback) {
        apiService.getProducts(page, limit, keyword, categoryId, brandId, priceMin, priceMax, sort).enqueue(callback);
    }
    public void addCartItem(int variantId, int quantity, Callback<CartResponse> callback) {
        apiService.addCartItem(new AddCartItemRequest(variantId, quantity)).enqueue(callback);
    }
}
