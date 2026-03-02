package com.example.itstore.activity;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.itstore.R;

import com.example.itstore.databinding.ActivityRegisterBinding;

public class RegisterActivity extends AppCompatActivity {

    private ActivityRegisterBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Chuyển về trang Đăng nhập khi nhấn vào "Đăng nhập"

        binding.tvLoginNow.setOnClickListener(v -> {
            finish();
        });

        // Chuyển về trang Đăng nhập khi nhấn vào nút Back
        binding.btnBack.setOnClickListener(v -> {
            finish();
        });
    }
}