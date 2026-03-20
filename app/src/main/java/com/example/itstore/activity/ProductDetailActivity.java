package com.example.itstore.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.itstore.R;
import com.example.itstore.databinding.ActivityProductDetailBinding;
import com.example.itstore.adapter.ImagePagerAdapter;
import com.example.itstore.model.CartItem;
import com.example.itstore.model.Product;
import com.example.itstore.utils.CartManager;
import com.example.itstore.viewmodel.ProductDetailViewModel;
public class ProductDetailActivity extends AppCompatActivity {
    private ActivityProductDetailBinding binding;
    private Product currentProduct;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        binding = ActivityProductDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        // 1. Hứng dữ liệu từ trang chủ
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("PRODUCT_INFO")) {
            currentProduct = (Product) intent.getSerializableExtra("PRODUCT_INFO");
        }
        if (currentProduct == null) {
            android.widget.Toast.makeText(this, "Lỗi: Không tải được thông tin sản phẩm!", android.widget.Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        // 2. Khởi tạo ViewModel và biến theo dõi giỏ hàng
        ProductDetailViewModel viewModel = new androidx.lifecycle.ViewModelProvider(this).get(ProductDetailViewModel.class);
        final int[] selectedVariantId = {1};
        final String[] selectedRam = {"8GB"};
        double basePrice = currentProduct.getPrice();
        double baseOldPrice = currentProduct.getCompareAtPrice();
        final double[] currentFinalPrice = {basePrice};
        // 3. Ánh xạ view và đổ dữ liệu
        binding.tvPriceOld.setPaintFlags(binding.tvPriceOld.getPaintFlags() | android.graphics.Paint.STRIKE_THRU_TEXT_FLAG);
        binding.tvProductName.setText(currentProduct.getName());
        updatePriceDisplay(currentProduct.getPrice(), currentProduct.getCompareAtPrice());
        // 4. Load hình ảnh mẫu
        java.util.List<Integer> listImages = new java.util.ArrayList<>();
        listImages.add(R.drawable.ram1);
        ImagePagerAdapter imageAdapter = new ImagePagerAdapter(listImages);
        binding.imgProductDetail.setAdapter(imageAdapter);
        // Nút Back
        binding.ivBack.setOnClickListener(v -> finish());
        // 5. Xử lý chọn cấu hình
        binding.cardChoice1.setOnClickListener(v -> {
            binding.cardChoice1.setBackgroundResource(R.color.orange_primary);
            binding.cardChoice1.setTextColor(androidx.core.content.ContextCompat.getColor(this, R.color.white));
            binding.cardChoice2.setBackgroundResource(android.R.color.transparent);
            binding.cardChoice2.setTextColor(androidx.core.content.ContextCompat.getColor(this, R.color.dark_gray));
            updatePriceDisplay(basePrice, baseOldPrice);
            selectedVariantId[0] = 1;
            selectedRam[0] = "8GB";
            currentFinalPrice[0] = basePrice;
        });
        binding.cardChoice2.setOnClickListener(v -> {
            binding.cardChoice2.setBackgroundResource(R.color.orange_primary);
            binding.cardChoice2.setTextColor(androidx.core.content.ContextCompat.getColor(this, R.color.white));
            binding.cardChoice1.setBackgroundResource(android.R.color.transparent);
            binding.cardChoice1.setTextColor(androidx.core.content.ContextCompat.getColor(this, R.color.dark_gray));
            updatePriceDisplay(basePrice + 500000, baseOldPrice + 500000);
            selectedVariantId[0] = 2;
            selectedRam[0] = "16GB";
            currentFinalPrice[0] = basePrice + 500000;
        });
        // 6. Nút thêm vào giỏ hàng
        binding.btnAddToCart.setOnClickListener(v -> {
            viewModel.addToCart(currentProduct, selectedVariantId[0], selectedRam[0], currentFinalPrice[0]);
        });
        binding.ivCart.setOnClickListener(v -> {
            android.content.Intent cartIntent = new android.content.Intent(ProductDetailActivity.this, CartActivity.class);
            startActivity(cartIntent);
        });
    }
    private void updatePriceDisplay(double newPrice, double oldPrice) {
        binding.tvPriceNew.setText(String.format(java.util.Locale.US, "%,.0f VNĐ", newPrice));
        binding.tvPriceOld.setText(String.format(java.util.Locale.US, "%,.0f VNĐ", oldPrice));
    }
}