package com.example.itstore.viewmodel;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import java.util.ArrayList;
import java.util.List;
import com.example.itstore.R;
import com.example.itstore.api.RetrofitClient;
import com.example.itstore.model.Brand;
import com.example.itstore.model.BrandResponse;
import com.example.itstore.model.Category;
import com.example.itstore.model.CategoryResponse;
import com.example.itstore.model.MockDataRepository;
import com.example.itstore.model.Product;
import com.example.itstore.model.ProductImage;
import com.example.itstore.model.ProductResponse;
import com.example.itstore.model.ProductVariant;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
    public void fetchCategories(Context context) {
        RetrofitClient.ApiService apiService = RetrofitClient.getApiService(context);

        apiService.getCategories().enqueue(new Callback<CategoryResponse>() {
            @Override
            public void onResponse(Call<CategoryResponse> call, Response<CategoryResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    List<Category> apiCategories = response.body().getData();
                    List<Category> finalCategories = new ArrayList<>();
                    finalCategories.add(new Category(-1, "Tất cả", ""));
                    finalCategories.addAll(apiCategories);
                    categoryListLiveData.setValue(finalCategories);
                }
            }
            @Override
            public void onFailure(Call<CategoryResponse> call, Throwable t) {
                Log.e("API_ERR", "Lỗi lấy danh mục Home: " + t.getMessage());
            }
        });
    }
    public void fetchSuggestedProducts(Context context) {
        RetrofitClient.getApiService(context).getProducts(
                1, 20, null, null, null, null, null, "newest"
        ).enqueue(new Callback<ProductResponse>() {
            @Override
            public void onResponse(Call<ProductResponse> call, Response<ProductResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    List<Product> apiProducts = response.body().getData();
                    productListLiveData.setValue(apiProducts);

                    allProducts = apiProducts;
                }
            }

            @Override
            public void onFailure(Call<ProductResponse> call, Throwable t) {
                Log.e("API_ERR", "Lỗi lấy sản phẩm Gợi ý: " + t.getMessage());
            }
        });
    }
}


