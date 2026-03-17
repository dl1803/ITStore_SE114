package com.example.itstore.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
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

        binding.btnLogin.setOnClickListener(v -> {
            Intent intent = new Intent(VerifyEmailActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });

        handleEmailLink(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        handleEmailLink(intent);
    }

    private void handleEmailLink(Intent intent) {
        Uri data = intent.getData();

        if (data != null){
            String token = data.getQueryParameter("token");
            if (token != null){
                verifyEmailViewModel.verifyEmailToken(token);
            } else {
                Toast.makeText(this, "Xác thực không hợp lệ!", Toast.LENGTH_LONG).show();
            }
        }
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

        verifyEmailViewModel.getIsVerifying().observe(this, isVerifying -> {
            if (isVerifying) {
                binding.btnLogin.setEnabled(false);
                binding.btnLogin.setText("Đang xác thực...");
            }
        }
        );

        verifyEmailViewModel.getSuccessMessage().observe(this , message -> {
            if (message != null) {
                new AlertDialog.Builder(this)
                        .setTitle("Thành công")
                        .setMessage(message)
                        .setPositiveButton("Đăng nhập ngay", (dialog, which) -> {
                            Intent intent = new Intent(VerifyEmailActivity.this, LoginActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();
                        })
                .setCancelable(false)
                .show();
            }
        });

        verifyEmailViewModel.getErrorMessage().observe(this, error -> {
            if (error != null) {
                new AlertDialog.Builder(this)
                        .setTitle("Lỗi xác thực")
                        .setMessage(error)
                        .setPositiveButton("Đóng", null)
                        .show();
            }
        });
    }
}