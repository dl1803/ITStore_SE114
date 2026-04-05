package com.example.itstore.activity;

import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.itstore.R;
import com.example.itstore.databinding.ActivitySettingNotiBinding;

public class NotificationSettingsActivity extends AppCompatActivity {

    private ActivitySettingNotiBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySettingNotiBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.imgBack.setOnClickListener(v -> finish());

        binding.switchOrderNoti.setOnCheckedChangeListener(
                (buttonView, isChecked) -> {
                    if (isChecked) {
                        Toast.makeText(this, "Đã bật thông báo đơn hàng", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "Đã tắt thông báo đơn hàng", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        binding.switchPromoNoti.setOnCheckedChangeListener(
                (buttonView, isChecked) -> {
                   if (isChecked) {
                       Toast.makeText(this, "Đã bật thông báo khuyến mãi", Toast.LENGTH_SHORT).show();
                   } else {
                       Toast.makeText(this, "Đã tắt thông báo khuyến mãi", Toast.LENGTH_SHORT).show();
                   }
        });
    }
}