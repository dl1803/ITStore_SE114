package com.example.itstore.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.itstore.R;
import com.example.itstore.databinding.ActivityProductDetailBinding;
import com.example.itstore.adapter.ImagePagerAdapter;
import com.example.itstore.fragment.SpecsBottomSheet;
import com.example.itstore.model.Product;
import com.example.itstore.viewmodel.ProductDetailViewModel;

import java.util.ArrayList;
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
        binding.tvXemCauHinhChiTiet.setOnClickListener(v -> {
            if (currentProduct != null) {
                SpecsBottomSheet bottomSheet = new SpecsBottomSheet(currentProduct);
                bottomSheet.show(getSupportFragmentManager(), "SpecsBottomSheet");
            }
        });
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