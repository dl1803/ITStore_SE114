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

public class ProductDetailActivity extends AppCompatActivity {
    private Product currentProduct;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("PRODUCT_INFO")) {
            currentProduct = (Product) intent.getSerializableExtra("PRODUCT_INFO");

            Toast.makeText(this, "Đã nhận được hàng: " + currentProduct.getName(), Toast.LENGTH_SHORT).show();
        }
        androidx.viewpager2.widget.ViewPager2 viewPagerImages = findViewById(R.id.imgProductDetail);
        android.widget.TextView tvName = findViewById(R.id.tvProductName);
        android.widget.TextView tvPrice = findViewById(R.id.tvPriceNew);
        android.widget.TextView tvOldPrice = findViewById(R.id.tvPriceOld);
        tvOldPrice.setPaintFlags(tvOldPrice.getPaintFlags() | android.graphics.Paint.STRIKE_THRU_TEXT_FLAG);
        tvName.setText(currentProduct.getName());
        android.view.View btnBack = findViewById(R.id.ivBack);
        updatePriceDisplay(currentProduct.getPrice(), currentProduct.getCompareAtPrice());
        java.util.List<Integer> listImages = new java.util.ArrayList<>();
        listImages.add(R.drawable.ram1);
        ImagePagerAdapter imageAdapter = new ImagePagerAdapter(listImages);
        viewPagerImages.setAdapter(imageAdapter);
        btnBack.setOnClickListener(new android.view.View.OnClickListener(){
            @Override
            public void onClick (android.view.View v){
                finish();
            }
        });
        android.widget.TextView cardChoice1 = findViewById(R.id.cardChoice1);
        android.widget.TextView cardChoice2 = findViewById(R.id.cardChoice2);
        double basePrice = currentProduct.getPrice();
        double baseOldPrice = currentProduct.getCompareAtPrice();
        cardChoice1.setOnClickListener(v -> {
            cardChoice1.setBackgroundResource(R.color.orange_primary);
            cardChoice1.setTextColor(androidx.core.content.ContextCompat.getColor(this, R.color.white));
            cardChoice2.setBackgroundResource(android.R.color.transparent);
            cardChoice2.setTextColor(androidx.core.content.ContextCompat.getColor(this, R.color.dark_gray));
            updatePriceDisplay(basePrice, baseOldPrice);
        });
        cardChoice2.setOnClickListener(v -> {
            cardChoice2.setBackgroundResource(R.color.orange_primary);
            cardChoice2.setTextColor(androidx.core.content.ContextCompat.getColor(this, R.color.white));
            cardChoice1.setBackgroundResource(android.R.color.transparent);
            cardChoice1.setTextColor(androidx.core.content.ContextCompat.getColor(this, R.color.dark_gray));
            updatePriceDisplay(basePrice + 500000, baseOldPrice + 500000);
        });

    }
    private void updatePriceDisplay(double newPrice, double oldPrice) {
        java.text.DecimalFormat formatter = new java.text.DecimalFormat("#,###");
        android.widget.TextView tvPrice = findViewById(R.id.tvPriceNew);
        tvPrice.setText(String.format(java.util.Locale.US, "%,.0f VNĐ", newPrice));
        android.widget.TextView tvOldPrice = findViewById(R.id.tvPriceOld);
        tvPrice.setText(String.format(java.util.Locale.US, "%,.0f VNĐ", oldPrice));
    }
}