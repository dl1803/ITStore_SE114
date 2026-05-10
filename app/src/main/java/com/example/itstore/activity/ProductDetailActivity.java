package com.example.itstore.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.itstore.R;
import com.example.itstore.adapter.ReviewAdapter;
import com.example.itstore.api.RetrofitClient;
import com.example.itstore.databinding.ActivityProductDetailBinding;
import com.example.itstore.adapter.ImagePagerAdapter;
import com.example.itstore.fragment.SpecsBottomSheet;
import com.example.itstore.model.CartItem;
import com.example.itstore.model.MockDataRepository;
import com.example.itstore.model.Product;
import com.example.itstore.model.ProductImage;
import com.example.itstore.model.ProductVariant;
import com.example.itstore.model.Review;
import com.example.itstore.model.SingleProductResponse;
import com.example.itstore.model.Specification;
import com.example.itstore.utils.CartManager;
import com.example.itstore.utils.SharedPrefsManager;
import com.example.itstore.viewmodel.ProductDetailViewModel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductDetailActivity extends AppCompatActivity {
    private ActivityProductDetailBinding binding;
    private Product currentProduct;
    private Product fullProductData;
    private ProductDetailViewModel detailViewModel;
    private int currentVariantId = 0;
    private String currentVariantName = "";
    private double currentFinalPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProductDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        detailViewModel = new androidx.lifecycle.ViewModelProvider(this).get(ProductDetailViewModel.class);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("PRODUCT_INFO")) {
            currentProduct = (Product) intent.getSerializableExtra("PRODUCT_INFO");
        }

        if (currentProduct == null) {
            Toast.makeText(this, "Lỗi: Không tải được thông tin sản phẩm!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        double basePrice = currentProduct.getPrice();
        double baseOldPrice = currentProduct.getCompareAtPrice();
        currentFinalPrice = basePrice;

        binding.tvPriceOld.setPaintFlags(binding.tvPriceOld.getPaintFlags() | android.graphics.Paint.STRIKE_THRU_TEXT_FLAG);
        binding.tvProductName.setText(currentProduct.getName());
        updatePriceDisplay(basePrice, baseOldPrice);

        List<String> listImages = new ArrayList<>();
        if (currentProduct != null && currentProduct.getImageUrl() != null) {
            listImages.add(currentProduct.getImageUrl());
        }
        ImagePagerAdapter imageAdapter = new ImagePagerAdapter(listImages);
        binding.imgProductDetail.setAdapter(imageAdapter);

        if (currentProduct.getSlug() != null && !currentProduct.getSlug().isEmpty()) {
            fetchFullProductDetail(currentProduct.getSlug());
        } else {
            Toast.makeText(this, "Sản phẩm không có dữ liệu chi tiết!", Toast.LENGTH_SHORT).show();
        }

        updateFavoriteIcon(currentProduct.isFavorite());

        binding.ivBack.setOnClickListener(v -> finish());
        binding.ivCart.setOnClickListener(v ->{
            goToCartScreen();
        });


        binding.btnAddToCart.setOnClickListener(v -> {
            if (fullProductData == null || currentVariantId == 0) {
                Toast.makeText(this, "Đang tải dữ liệu từ Server...", Toast.LENGTH_SHORT).show();
                return;
            }
            String token = SharedPrefsManager.getInstance(this).getAccessToken();
            if (token == null || token.isEmpty()) {
                Toast.makeText(this, "Vui lòng đăng nhập để thêm vào giỏ hàng!", Toast.LENGTH_SHORT).show();
                Intent intentLogin = new Intent(this, LoginActivity.class);
                startActivity(intentLogin);
            } else {
                detailViewModel.addToCart(fullProductData, currentVariantId, currentVariantName, 1);
                Toast.makeText(ProductDetailActivity.this, "Đã thêm bản " + currentVariantName + " vào giỏ!", Toast.LENGTH_SHORT).show();
            }
        });

        binding.btnBuyNow.setOnClickListener(v -> {
            if (fullProductData == null || currentVariantId == 0) return;
            CartItem buyNowItem = new CartItem(
                    0,
                    0,
                    currentVariantId,
                    1,
                    fullProductData,
                    currentVariantName,
                    currentFinalPrice
            );
            buyNowItem.setSelected(true);

            List<CartItem> fastCheckoutList = new ArrayList<>();
            fastCheckoutList.add(buyNowItem);

            CartManager.getInstance().setCheckoutList(fastCheckoutList);

            Intent intentBuyNow = new Intent(ProductDetailActivity.this, CheckoutActivity.class);
            startActivity(intentBuyNow);
        });


        binding.imgFavoriteItem.setOnClickListener(v -> {
            String token = SharedPrefsManager.getInstance(this).getAccessToken();

            if (token == null || token.isEmpty()) {
                Toast.makeText(this, "Vui lòng đăng nhập để lưu sản phẩm yêu thích!", Toast.LENGTH_SHORT).show();
                Intent intentLogin = new Intent(this, LoginActivity.class);
                startActivity(intentLogin);
            } else {
                boolean newStatus = !currentProduct.isFavorite();
                currentProduct.setFavorite(newStatus);
                updateFavoriteIcon(newStatus);
                MockDataRepository.getInstance().updateProduct(currentProduct);
                Intent returnIntent = new Intent();
                returnIntent.putExtra("UPDATED_PRODUCT", currentProduct);
                setResult(RESULT_OK, returnIntent);
                Toast.makeText(this, newStatus ? "Đã thêm vào yêu thích" : "Đã xóa khỏi yêu thích", Toast.LENGTH_SHORT).show();
            }
        });
        binding.tvXemCauHinhChiTiet.setOnClickListener(v -> {
            if (fullProductData != null) {
                SpecsBottomSheet bottomSheet = new SpecsBottomSheet(fullProductData);
                bottomSheet.show(getSupportFragmentManager(), "SpecsBottomSheet");
            } else {
                Toast.makeText(this, "Đang tải dữ liệu, vui lòng đợi...", Toast.LENGTH_SHORT).show();
            }
        });
        // Lấy dữ liệu mô tả thực tế từ sản phẩm
        if (currentProduct != null && currentProduct.getDescription() != null && !currentProduct.getDescription().isEmpty()) {
            binding.tvProductContent.setText(currentProduct.getDescription());
        }
        final boolean[] isDescExpanded = {false};

        View.OnClickListener toggleDescriptionListener = v -> {
            if (isDescExpanded[0]) {
                binding.tvProductContent.setMaxLines(3);
                binding.XemThem.setText("Xem thêm");
                isDescExpanded[0] = false;
            } else {
                binding.tvProductContent.setMaxLines(Integer.MAX_VALUE);
                binding.XemThem.setText("Thu gọn");
                isDescExpanded[0] = true;
            }
        };
        binding.XemThem.setOnClickListener(toggleDescriptionListener);
        binding.tvProductContent.setOnClickListener(toggleDescriptionListener);
        setupReview();
    }
    private void fetchFullProductDetail(String slug) {
        RetrofitClient.getApiService(this).getProductBySlug(slug)
                .enqueue(new Callback<SingleProductResponse>() {
                    @Override
                    public void onResponse(Call<SingleProductResponse> call, Response<SingleProductResponse> response) {
                        if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                            fullProductData = response.body().getData();
                            setupDynamicData(); // lấy data từ api xong thì cập nhật lại giao diện
                        }
                    }

                    @Override
                    public void onFailure(Call<SingleProductResponse> call, Throwable t) {
                        Log.e("API_ERR", "Lỗi tải chi tiết sản phẩm: " + t.getMessage());
                    }
                });
    }
    private void setupDynamicData() {
        if (fullProductData.getDescription() != null ) {
            binding.tvProductContent.setText(fullProductData.getDescription());
        }
        List<String> listImagesUrls = new ArrayList<>();
        if (fullProductData.getImages() != null && !fullProductData.getImages().isEmpty()) {
            for (ProductImage img : fullProductData.getImages()) {
                listImagesUrls.add(img.getImageUrl());
            }
        }
        ImagePagerAdapter imageAdapter = new ImagePagerAdapter(listImagesUrls);
        binding.imgProductDetail.setAdapter(imageAdapter);

        List<ProductVariant> variants = fullProductData.getVariants();
        if (variants != null && !variants.isEmpty()) {
            // Mặc định chọn bản đầu tiên
            ProductVariant var1 = variants.get(0);
            currentVariantId = var1.getId();
            currentVariantName = var1.getVersion();
            currentFinalPrice = var1.getPrice();
            updatePriceDisplay(currentFinalPrice, var1.getCompareAtPrice());

            binding.cardChoice1.setVisibility(View.VISIBLE);
            binding.cardChoice1.setText(currentVariantName);

            binding.cardChoice1.setOnClickListener(v -> {
                binding.cardChoice1.setBackgroundResource(R.color.orange_primary);
                binding.cardChoice1.setTextColor(ContextCompat.getColor(this, R.color.white));
                binding.cardChoice2.setBackgroundResource(android.R.color.transparent);
                binding.cardChoice2.setTextColor(ContextCompat.getColor(this, R.color.dark_gray));

                currentVariantId = var1.getId();
                currentVariantName = var1.getVersion();
                currentFinalPrice = var1.getPrice();
                updatePriceDisplay(currentFinalPrice, var1.getCompareAtPrice());
            });

            // Nếu có bản thứ 2
            if (variants.size() > 1) {
                ProductVariant var2 = variants.get(1);
                binding.cardChoice2.setVisibility(View.VISIBLE);

                binding.cardChoice2.setText(var2.getVersion());

                binding.cardChoice2.setOnClickListener(v -> {
                    binding.cardChoice2.setBackgroundResource(R.color.orange_primary);
                    binding.cardChoice2.setTextColor(ContextCompat.getColor(this, R.color.white));
                    binding.cardChoice1.setBackgroundResource(android.R.color.transparent);
                    binding.cardChoice1.setTextColor(ContextCompat.getColor(this, R.color.dark_gray));

                    currentVariantId = var2.getId();
                    currentVariantName = var2.getVersion();
                    currentFinalPrice = var2.getPrice();
                    updatePriceDisplay(currentFinalPrice, var2.getCompareAtPrice());
                });
            } else {
                binding.cardChoice2.setVisibility(View.GONE);
            }
        }
        List<Specification> specs = fullProductData.getSpecificationList();

        if (specs != null && !specs.isEmpty()) {
            binding.layoutHighlightSpecs.setVisibility(View.VISIBLE);

            binding.tvHighlightKey1.setText(""); binding.tvHighlightValue1.setText("");
            binding.tvHighlightKey2.setText(""); binding.tvHighlightValue2.setText("");
            binding.tvHighlightKey3.setText(""); binding.tvHighlightValue3.setText("");

            if (specs.size() > 0) {
                binding.tvHighlightKey1.setText(specs.get(0).getKey().toUpperCase() + ":");
                binding.tvHighlightValue1.setText(specs.get(0).getValue());
            }

            if (specs.size() > 1) {
                binding.tvHighlightKey2.setText(specs.get(1).getKey().toUpperCase() + ":");
                binding.tvHighlightValue2.setText(specs.get(1).getValue());
            }

            if (specs.size() > 2) {
                binding.tvHighlightKey3.setText(specs.get(2).getKey().toUpperCase() + ":");
                binding.tvHighlightValue3.setText(specs.get(2).getValue());
            }
        } else {

            binding.layoutHighlightSpecs.setVisibility(View.GONE);
        }
    }
    private void setupReview(){
        List<Review> allReviews = getReviews();

        if (allReviews == null && !allReviews.isEmpty()) {
            binding.tvRatingCount.setText("O bài đánh giá");
            binding.tvSeeAllReviews.setText("Xem tất cả (0) >");
            return;
        }

        Collections.sort(allReviews, new Comparator<Review>(){
            @Override
            public int compare(Review o1, Review o2) {
                return Long.compare(o2.getTimestamp(), o1.getTimestamp());
            }
        });

        int totalReviews = allReviews.size();
        binding.tvSeeAllReviews.setText("Xem tất cả (" + totalReviews + ") >");
        binding.tvRatingCount.setText(totalReviews + " bài đánh giá");

        int limit = Math.min(3, totalReviews);
        List<Review> top3Reviews = new ArrayList<>(allReviews.subList(0,limit));

        ReviewAdapter adapter = new ReviewAdapter(top3Reviews);
        binding.rvProductReviews.setLayoutManager(new LinearLayoutManager(this));
        binding.rvProductReviews.setAdapter(adapter);

        binding.tvSeeAllReviews.setOnClickListener( v -> {
            Intent intent = new Intent(ProductDetailActivity.this, ProductReviewsActivity.class);
            intent.putExtra("REVIEW_LIST", (Serializable) allReviews); // ép kiểu List<Review> thành Serializable để nhấn mạnh dữ liệu có thể được đơn giản hóa qua intent , tránh lỗi
            startActivity(intent);
        });

    }

    private List<Review> getReviews() {
        List<Review> mockReviews = new ArrayList<>();
        long curTime = System.currentTimeMillis(); // Lấy thời gian hiện tại
        mockReviews.add(new Review(1, "User 1", 5.0f, "Tuyệt vời", curTime, "22/02/2026"));
        mockReviews.add(new Review(2, "User 2", 4.0f, "Khá tốt", curTime - 100000, "21/02/2026"));
        mockReviews.add(new Review(3, "User 3", 5.0f, "Đóng gói đẹp", curTime - 200000, "20/02/2026"));
        mockReviews.add(new Review(4, "User 4", 3.0f, "Bình thường", curTime - 300000, "19/02/2026"));
        mockReviews.add(new Review(5, "User 5", 5.0f, "Rất ưng ý", curTime - 400000, "18/02/2026"));
        return mockReviews;
    }
    private void updateFavoriteIcon(boolean isFav) {
        int color = isFav ? android.graphics.Color.parseColor("#FF9800") : android.graphics.Color.parseColor("#B3B3B3");
        binding.imgFavoriteItem.setColorFilter(color);
    }

    private void goToCartScreen() {
        Intent intent = new Intent(ProductDetailActivity.this, MainActivity.class);
        intent.putExtra("navigate_to", "cart");
        intent.putExtra("from_detail", true);
        startActivity(intent);
    }

    private void updatePriceDisplay(double newPrice, double oldPrice) {
        binding.tvPriceNew.setText(String.format(Locale.US, "%,.0f VNĐ", newPrice));
        binding.tvPriceOld.setText(String.format(Locale.US, "%,.0f VNĐ", oldPrice));
    }
}