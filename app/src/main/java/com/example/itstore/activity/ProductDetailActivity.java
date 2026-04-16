package com.example.itstore.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.itstore.R;
import com.example.itstore.adapter.ReviewAdapter;
import com.example.itstore.databinding.ActivityProductDetailBinding;
import com.example.itstore.adapter.ImagePagerAdapter;
import com.example.itstore.model.CartItem;
import com.example.itstore.model.Product;
import com.example.itstore.model.Review;
import com.example.itstore.utils.CartManager;
import com.example.itstore.viewmodel.ProductDetailViewModel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

public class ProductDetailActivity extends AppCompatActivity {
    private ActivityProductDetailBinding binding;
    private Product currentProduct;
    private ProductDetailViewModel detailViewModel;
    private int currentVariantId = 1;
    private String currentRam = "8GB";
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

        List<Integer> listImages = new ArrayList<>();
        listImages.add(R.drawable.ram1);
        ImagePagerAdapter imageAdapter = new ImagePagerAdapter(listImages);
        binding.imgProductDetail.setAdapter(imageAdapter);

        updateFavoriteIcon(currentProduct.isFavorite());

        binding.ivBack.setOnClickListener(v -> finish());

        binding.cardChoice1.setOnClickListener(v -> {
            binding.cardChoice1.setBackgroundResource(R.color.orange_primary);
            binding.cardChoice1.setTextColor(ContextCompat.getColor(this, R.color.white));
            binding.cardChoice2.setBackgroundResource(android.R.color.transparent);
            binding.cardChoice2.setTextColor(ContextCompat.getColor(this, R.color.dark_gray));
            updatePriceDisplay(basePrice, baseOldPrice);
            currentVariantId = 1;
            currentRam = "8GB";
            currentFinalPrice = basePrice;
        });

        binding.cardChoice2.setOnClickListener(v -> {
            binding.cardChoice2.setBackgroundResource(R.color.orange_primary);
            binding.cardChoice2.setTextColor(ContextCompat.getColor(this, R.color.white));
            binding.cardChoice1.setBackgroundResource(android.R.color.transparent);
            binding.cardChoice1.setTextColor(ContextCompat.getColor(this, R.color.dark_gray));
            updatePriceDisplay(basePrice + 500000, baseOldPrice + 500000);
            currentVariantId = 2;
            currentRam = "16GB";
            currentFinalPrice = basePrice + 500000;
        });

        binding.btnAddToCart.setOnClickListener(v -> {
            detailViewModel.addToCart(currentProduct, currentVariantId, currentRam, currentFinalPrice);
            Toast.makeText(ProductDetailActivity.this, "Đã thêm bản " + currentRam + " vào giỏ!", Toast.LENGTH_SHORT).show();
        });

        binding.btnBuyNow.setOnClickListener(v -> {
            CartItem buyNowItem = new CartItem(
                    0,
                    0,
                    currentVariantId,
                    1,
                    currentProduct,
                    currentRam,
                    currentFinalPrice
            );
            buyNowItem.setSelected(true);

            List<CartItem> fastCheckoutList = new ArrayList<>();
            fastCheckoutList.add(buyNowItem);

            CartManager.getInstance().setCheckoutList(fastCheckoutList);

            Intent intentBuyNow = new Intent(ProductDetailActivity.this, CheckoutActivity.class);
            startActivity(intentBuyNow);
        });

        binding.ivCart.setOnClickListener(v -> goToCartScreen());

        binding.imgFavoriteItem.setOnClickListener(v -> {
            boolean newStatus = !currentProduct.isFavorite();
            currentProduct.setFavorite(newStatus);
            updateFavoriteIcon(newStatus);
            Intent returnIntent = new Intent();
            returnIntent.putExtra("UPDATED_PRODUCT", currentProduct);
            setResult(RESULT_OK, returnIntent);

            Toast.makeText(this, newStatus ? "Đã thêm vào yêu thích" : "Đã xóa khỏi yêu thích", Toast.LENGTH_SHORT).show();
        });

        setupReview();
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
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }

    private void updatePriceDisplay(double newPrice, double oldPrice) {
        binding.tvPriceNew.setText(String.format(Locale.US, "%,.0f VNĐ", newPrice));
        binding.tvPriceOld.setText(String.format(Locale.US, "%,.0f VNĐ", oldPrice));
    }
}