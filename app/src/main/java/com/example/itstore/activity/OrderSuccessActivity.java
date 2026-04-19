package com.example.itstore.activity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.itstore.R;
import com.example.itstore.databinding.ActivityOrderSuccessBinding;
import com.example.itstore.model.Order;

public class OrderSuccessActivity extends AppCompatActivity {

    private ActivityOrderSuccessBinding binding;
    private Order order;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOrderSuccessBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        order = (Order) getIntent().getSerializableExtra("ORDER_DATA");

        if (order != null) {
            binding.tvOrder.setText("#" + order.getOrderId());
        } else {
            binding.tvOrder.setText("#LỖI_KHÔNG_CÓ_MÃ");
        }
        setupClickListeners();
    }

    private void setupClickListeners() {
        binding.btnCopyOrder.setOnClickListener(v -> {
            if (order != null) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("Mã đơn hàng", order.getOrderId());
                clipboard.setPrimaryClip(clip);

                Toast.makeText(this, "Đã copy mã đơn hàng!", Toast.LENGTH_SHORT).show();
            }
        });

        binding.btnContinueBuy.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            finish();
        });

        binding.tvViewDetail.setOnClickListener(v -> {
            Intent intent = new Intent(this, OrderDetailActivity.class);
            intent.putExtra("ORDER_DATA", order);
            startActivity(intent);
            finish();
        });
    }
}