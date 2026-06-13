package com.example.itstore.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.itstore.databinding.ActivitySettingNotiBinding;

public class NotificationSettingsActivity extends AppCompatActivity {

    private ActivitySettingNotiBinding binding;
    private SharedPreferences sharedPreferences;

    private static final String PREFS_NAME = "NotificationPrefs";
    private static final String KEY_ORDER_NOTI = "order_noti";
    private static final String KEY_PROMO_NOTI = "promo_noti";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySettingNotiBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        binding.imgBack.setOnClickListener(v -> finish());
        boolean isOrderEnabled = sharedPreferences.getBoolean(KEY_ORDER_NOTI, true);
        boolean isPromoEnabled = sharedPreferences.getBoolean(KEY_PROMO_NOTI, true);
        binding.switchOrderNoti.setChecked(isOrderEnabled);
        binding.switchPromoNoti.setChecked(isPromoEnabled);
        binding.switchOrderNoti.setOnCheckedChangeListener(
                (buttonView, isChecked) -> {
                    sharedPreferences.edit().putBoolean(KEY_ORDER_NOTI, isChecked).apply();
                    if (isChecked) {
                        Toast.makeText(this, "Đã bật thông báo đơn hàng", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "Đã tắt thông báo đơn hàng", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        binding.switchPromoNoti.setOnCheckedChangeListener(
                (buttonView, isChecked) -> {
                    sharedPreferences.edit().putBoolean(KEY_PROMO_NOTI, isChecked).apply();
                    if (isChecked) {
                        Toast.makeText(this, "Đã bật thông báo khuyến mãi", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "Đã tắt thông báo khuyến mãi", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}