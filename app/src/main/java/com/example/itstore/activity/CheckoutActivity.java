package com.example.itstore.activity;

import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.itstore.R;
import com.example.itstore.databinding.ActivityCheckoutBinding;
import com.example.itstore.model.CartItem;
import com.example.itstore.utils.CartManager;

import java.text.DecimalFormat;
import java.util.List;

public class CheckoutActivity extends AppCompatActivity {

    ActivityCheckoutBinding binding;
    private double totalGoodsPrice = 0;
    private double shippingFee = 30000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        
        super.onCreate(savedInstanceState);
        binding = ActivityCheckoutBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());

        binding.btnBack.setOnClickListener(v -> finish());


        
        List<CartItem> receivedItems = CartManager.getInstance().getCheckoutList();

        if (receivedItems != null && !receivedItems.isEmpty()) {
            calculateAndDisplayPrice(receivedItems);
        } else {
            Toast.makeText(this, "Lỗi: Không tìm thấy sản phẩm nào!", Toast.LENGTH_SHORT).show();
            finish();
        }

        binding.btnCheckout.setOnClickListener(v -> {
            String paymentMethod = "COD";
            if (binding.rbMomo.isChecked()) paymentMethod = "MOMO";
            if (binding.rbBankTransfer.isChecked()) paymentMethod = "BANK";

            Toast.makeText(this, "Chốt đơn! Tổng: " + formatMoney(totalGoodsPrice + shippingFee)
                    + " | Trả bằng: " + paymentMethod, Toast.LENGTH_LONG).show();
        });
    }

    private void calculateAndDisplayPrice(List<CartItem> items) {
        totalGoodsPrice = 0;

        for (CartItem item : items) {
            totalGoodsPrice += (item.getPrice() * item.getQuantity());
        }

        double finalTotal = totalGoodsPrice + shippingFee;

        binding.tvSubtotalPrice.setText(formatMoney(totalGoodsPrice));
        binding.tvShippingPrice.setText(formatMoney(shippingFee));

        binding.tvTotalPrice.setText(formatMoney(finalTotal));
        binding.tvTotal.setText(formatMoney(finalTotal));

        binding.tvDiscount.setText("0 đ");
    }

    private String formatMoney(double amount) {
        DecimalFormat formatter = new DecimalFormat("###,###,###");
        return formatter.format(amount) + " đ";
    }
}