package com.example.itstore;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.itstore.adapter.ImagePagerAdapter;
import com.example.itstore.model.Product;
import com.example.itstore.viewmodel.ProductDetailViewModel;

public class ProductDetailActivity extends AppCompatActivity {
    private Product currentProduct;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
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
        final String[] selectedRam = {"8GB"};
        double basePrice = currentProduct.getPrice();
        double baseOldPrice = currentProduct.getCompareAtPrice();
        final double[] currentFinalPrice = {basePrice};
        // 3. Ánh xạ view và đổ dữ liệu
        androidx.viewpager2.widget.ViewPager2 viewPagerImages = findViewById(R.id.imgProductDetail);
        android.widget.TextView tvName = findViewById(R.id.tvProductName);
        android.widget.TextView tvOldPrice = findViewById(R.id.tvPriceOld);
        tvOldPrice.setPaintFlags(tvOldPrice.getPaintFlags() | android.graphics.Paint.STRIKE_THRU_TEXT_FLAG);
        tvName.setText(currentProduct.getName());
        updatePriceDisplay(currentProduct.getPrice(), currentProduct.getCompareAtPrice());
        // 4. Load hình ảnh mẫu
        java.util.List<Integer> listImages = new java.util.ArrayList<>();
        listImages.add(R.drawable.ram1);
        ImagePagerAdapter imageAdapter = new ImagePagerAdapter(listImages);
        viewPagerImages.setAdapter(imageAdapter);
        // Nút Back
        android.view.View btnBack = findViewById(R.id.ivBack);
        btnBack.setOnClickListener(new android.view.View.OnClickListener(){
            @Override
            public void onClick (android.view.View v){
                finish();
            }
        });
        // 5. Xử lý chọn cấu hình
        android.widget.TextView cardChoice1 = findViewById(R.id.cardChoice1);
        android.widget.TextView cardChoice2 = findViewById(R.id.cardChoice2);
        cardChoice1.setOnClickListener(v -> {
            cardChoice1.setBackgroundResource(R.color.orange_primary);
            cardChoice1.setTextColor(androidx.core.content.ContextCompat.getColor(this, R.color.white));
            cardChoice2.setBackgroundResource(android.R.color.transparent);
            cardChoice2.setTextColor(androidx.core.content.ContextCompat.getColor(this, R.color.dark_gray));
            updatePriceDisplay(basePrice, baseOldPrice);
            selectedRam[0] = "8GB";
            currentFinalPrice[0] = basePrice;
        });
        cardChoice2.setOnClickListener(v -> {
            cardChoice2.setBackgroundResource(R.color.orange_primary);
            cardChoice2.setTextColor(androidx.core.content.ContextCompat.getColor(this, R.color.white));
            cardChoice1.setBackgroundResource(android.R.color.transparent);
            cardChoice1.setTextColor(androidx.core.content.ContextCompat.getColor(this, R.color.dark_gray));
            updatePriceDisplay(basePrice + 500000, baseOldPrice + 500000);
            selectedRam[0] = "16GB";
            currentFinalPrice[0] = basePrice + 500000;
        });
        // 6. Nút thêm vào giỏ hàng
        android.widget.Button btnAddToCart = findViewById(R.id.btnAddToCart);
        if(btnAddToCart != null) {
            btnAddToCart.setOnClickListener(v -> {
                viewModel.addToCart(currentProduct, selectedRam[0], currentFinalPrice[0]);
                Toast.makeText(this, "Đã thêm bản " + selectedRam[0] + " vào giỏ!", Toast.LENGTH_SHORT).show();
            });
        }
    }
    private void updatePriceDisplay(double newPrice, double oldPrice) {
        android.widget.TextView tvPrice = findViewById(R.id.tvPriceNew);
        tvPrice.setText(String.format(java.util.Locale.US, "%,.0f VNĐ", newPrice));
        android.widget.TextView tvOldPrice = findViewById(R.id.tvPriceOld);
        tvOldPrice.setText(String.format(java.util.Locale.US, "%,.0f VNĐ", oldPrice));
    }
}