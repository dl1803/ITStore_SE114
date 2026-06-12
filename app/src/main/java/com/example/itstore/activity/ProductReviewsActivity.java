package com.example.itstore.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.chip.Chip;
import com.example.itstore.R;
import com.example.itstore.adapter.ReviewAdapter;
import com.example.itstore.api.RetrofitClient;
import com.example.itstore.databinding.ActivityReviewHistoryBinding;
import com.example.itstore.model.ProductReviewsResponse;
import com.example.itstore.model.ProductVariant;
import com.example.itstore.model.VariantReviewsResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductReviewsActivity extends AppCompatActivity {
    private ActivityReviewHistoryBinding binding;
    private ReviewAdapter adapter;
    private final List<ProductReviewsResponse.ReviewDetail> reviewList = new ArrayList<>();
    private int productId = -1;

    private List<ProductVariant> variantList = new ArrayList<>();
    private int selectedFilterPosition = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityReviewHistoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.imgBack.setOnClickListener(v -> finish());

        adapter = new ReviewAdapter(reviewList);
        binding.rvReviewHistory.setLayoutManager(new LinearLayoutManager(this));
        binding.rvReviewHistory.setAdapter(adapter);

        productId = getIntent().getIntExtra("PRODUCT_ID", -1);

        List<ProductVariant> incomingVariants = (List<ProductVariant>) getIntent().getSerializableExtra("PRODUCT_VARIANTS");
        if (incomingVariants != null) {
            variantList.addAll(incomingVariants);
        }
        setupVariantFilterChips();

        if (productId != -1) {
            loadAllProductReviews();
        } else {
            showEmptyUI();
        }
    }

    private void setupVariantFilterChips() {
        binding.chipGroupVariantFilter.removeAllViews();
        addMaterialFilterChip("Tất cả", 0, -1);
        if (variantList != null && !variantList.isEmpty()) {
            for (int i = 0; i < variantList.size(); i++) {
                ProductVariant variant = variantList.get(i);
                addMaterialFilterChip(variant.getVersion(), i + 1, variant.getId());
            }
        }
        if (binding.chipGroupVariantFilter.getChildCount() > selectedFilterPosition) {
            binding.chipGroupVariantFilter.check(binding.chipGroupVariantFilter.getChildAt(selectedFilterPosition).getId());
        }
    }
    private void addMaterialFilterChip(String title, int position, int variantId) {
        Chip chip = (Chip) getLayoutInflater().inflate(R.layout.item_chip_search, binding.chipGroupVariantFilter, false);
        chip.setText(title);
        chip.setId(position);
        chip.setOnClickListener(v -> {
            if (selectedFilterPosition != position) {
                selectedFilterPosition = position;
                if (variantId == -1) {
                    loadAllProductReviews();
                } else {
                    loadVariantReviews(variantId);
                }
            }
        });
        binding.chipGroupVariantFilter.addView(chip);
    }

    private void loadAllProductReviews() {
        RetrofitClient.getApiService(this).getProductReviews(productId, 1, 20).enqueue(new Callback<ProductReviewsResponse>() {
            @Override
            public void onResponse(Call<ProductReviewsResponse> call, Response<ProductReviewsResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<ProductReviewsResponse.ReviewDetail> serverReviews = response.body().getData();
                    updateReviewListUI(serverReviews);
                } else {
                    showEmptyUI();
                }
            }

            @Override
            public void onFailure(Call<ProductReviewsResponse> call, Throwable t) {
                Log.e("API_ALL_REVIEWS_ERROR", "Lỗi: " + t.getMessage());
                showEmptyUI();
            }
        });
    }

    private void loadVariantReviews(int variantId) {
        RetrofitClient.getApiService(this).getVariantReviews(variantId, 1, 20).enqueue(new Callback<VariantReviewsResponse>() {
            @Override
            public void onResponse(Call<VariantReviewsResponse> call, Response<VariantReviewsResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<ProductReviewsResponse.ReviewDetail> serverReviews = response.body().getData();
                    updateReviewListUI(serverReviews);
                } else {
                    showEmptyUI();
                }
            }

            @Override
            public void onFailure(Call<VariantReviewsResponse> call, Throwable t) {
                Log.e("API_VARIANT_REVIEWS_ERROR", "Lỗi lọc: " + t.getMessage());
                showEmptyUI();
            }
        });
    }

    private void updateReviewListUI(List<ProductReviewsResponse.ReviewDetail> reviews) {
        if (reviews != null && !reviews.isEmpty()) {
            reviewList.clear();
            reviewList.addAll(reviews);
            binding.layoutEmptyReview.setVisibility(View.GONE);
            binding.rvReviewHistory.setVisibility(View.VISIBLE);
            adapter.notifyDataSetChanged();
        } else {
            showEmptyUI();
        }
    }

    private void showEmptyUI() {
        reviewList.clear();
        adapter.notifyDataSetChanged();
        binding.layoutEmptyReview.setVisibility(View.VISIBLE);
        binding.rvReviewHistory.setVisibility(View.GONE);
    }
}