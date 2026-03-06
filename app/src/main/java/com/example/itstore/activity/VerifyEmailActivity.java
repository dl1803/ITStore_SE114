package com.example.itstore.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.itstore.R;
import com.example.itstore.databinding.ActivityVerifyEmailBinding;
import com.example.itstore.viewmodel.VerifyEmailViewModel;

public class VerifyEmailActivity extends AppCompatActivity {
    private ActivityVerifyEmailBinding binding;
    private VerifyEmailViewModel verifyEmailViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityVerifyEmailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Chuyển về màn hình đăng nhập
        binding.btnLogin.setOnClickListener(v -> {
            Intent intent = new Intent(VerifyEmailActivity.this, LoginActivity.class);
            startActivity(intent);
        });

        // Lắng nghe và xử lí sự kiện khi nhấn nút Gửi lại
        verifyEmailViewModel = new ViewModelProvider(this).get(VerifyEmailViewModel.class);

        setupObservers();

        binding.tvResendEmail.setOnClickListener(v -> {
            verifyEmailViewModel.resendEmail();
        });
    }
    private void setupObservers() {
        verifyEmailViewModel.getIsResending().observe(this, isResending -> {
            if (isResending) {
                binding.tvResendEmail.setEnabled(false);
                binding.tvResendEmail.setText("Đang gửi lại...");
            } else {
                binding.tvResendEmail.setEnabled(true);
                binding.tvResendEmail.setText("Gửi lại email");
            }
        });

        verifyEmailViewModel.getIsResendSuccess().observe(this, isSuccess -> {
            if (isSuccess) {
                Toast.makeText(this, "Đã gửi lại email xác thực!", Toast.LENGTH_LONG).show();
            }
        });

    }
}