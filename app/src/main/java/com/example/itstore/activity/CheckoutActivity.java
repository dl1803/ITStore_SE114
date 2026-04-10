package com.example.itstore.activity;

import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.itstore.R;
import com.example.itstore.adapter.CheckoutAdapter;
import com.example.itstore.databinding.ActivityCheckoutBinding;
import com.example.itstore.model.CartItem;
import com.example.itstore.utils.CartManager;
import com.example.itstore.viewmodel.CheckoutViewModel;

import java.text.DecimalFormat;
import java.util.List;

public class CheckoutActivity extends AppCompatActivity {

    ActivityCheckoutBinding binding;
    private CheckoutViewModel checkoutViewModel;
    private CheckoutAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        
        super.onCreate(savedInstanceState);
        binding = ActivityCheckoutBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());

        binding.btnBack.setOnClickListener(v -> finish());

        checkoutViewModel = new ViewModelProvider(this).get(CheckoutViewModel.class);
        binding.rvCheckoutItems.setLayoutManager(new LinearLayoutManager(this));

        observeViewModel();
        
        List<CartItem> receivedItems = CartManager.getInstance().getCheckoutList();

        if (receivedItems != null && !receivedItems.isEmpty()) {
            checkoutViewModel.loadCheckoutData(receivedItems);
        } else {
            Toast.makeText(this, "Lỗi: Không tìm thấy sản phẩm nào!", Toast.LENGTH_SHORT).show();
            finish();
        }

        binding.btnCheckout.setOnClickListener(v -> {
            String paymentMethod = "COD";
            if (binding.rbMomo.isChecked()) paymentMethod = "MOMO";
            if (binding.rbBankTransfer.isChecked()) paymentMethod = "BANK";

            double finalTotal = checkoutViewModel.getFinalTotalPrice().getValue() != null
                    ? checkoutViewModel.getFinalTotalPrice().getValue() : 0;

            Toast.makeText(this, "Chốt đơn: " + String.format("%,.0f đ", finalTotal)
                    + " | Qua: " + paymentMethod, Toast.LENGTH_LONG).show();
        });
    }

    private void observeViewModel() {
        checkoutViewModel.getCheckoutItems().observe(this, items -> {
            adapter = new CheckoutAdapter(items);
            binding.rvCheckoutItems.setAdapter(adapter);
            binding.rvCheckoutItems.setNestedScrollingEnabled(false);
        });

        checkoutViewModel.getSubtotalPrice().observe(this, price ->
                binding.tvSubtotalPrice.setText(String.format("%,.0f đ", price)));

        checkoutViewModel.getShippingFee().observe(this, fee ->
                binding.tvShippingPrice.setText(String.format("%,.0f đ", fee)));

        checkoutViewModel.getTotalDiscount().observe(this, discount ->
                binding.tvDiscount.setText(String.format("%,.0f đ", discount)));

        checkoutViewModel.getFinalTotalPrice().observe(this, finalTotal -> {
            String formattedTotal = String.format("%,.0f đ", finalTotal);
            binding.tvTotalPrice.setText(formattedTotal);
            binding.tvTotal.setText(formattedTotal);
        });
    }
}