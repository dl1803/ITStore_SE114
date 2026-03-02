package com.example.itstore.activity;

import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.itstore.R;

import com.example.itstore.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);


        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //Thông báo đã vào màn hình Đăng nhập
        Toast.makeText(this, "Đã vào màn hình Đăng nhập!", Toast.LENGTH_LONG).show();


        // Chuyển tới trang Đăng ký
        binding.tvRegister.setOnClickListener(v -> {
            android.content.Intent intent = new android.content.Intent(this, RegisterActivity.class);
            startActivity(intent);
        });
    }
}