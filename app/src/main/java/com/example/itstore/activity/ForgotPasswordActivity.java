package com.example.itstore.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.itstore.databinding.ActivityForgotPasswordBinding;

public class ForgotPasswordActivity extends AppCompatActivity {
    private ActivityForgotPasswordBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        binding = ActivityForgotPasswordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //Chuyển về màn hình đăng nhập
        binding.btnBack.setOnClickListener(view -> {
            Intent intent = new Intent(ForgotPasswordActivity.this, LoginActivity.class);
            startActivity(intent);
        });

        setupSendRequestLinkBtn();
    }

    private void setupSendRequestLinkBtn() {
        binding.btnSendResetLink.setOnClickListener(v -> {
            String email = binding.edtEmail.getText().toString().trim();

            if (email.isEmpty()) {
                binding.tilEmail.setError("Vui lòng nhập email");
                binding.tilEmail.requestFocus();
                return;
            }
            else if (!email.matches("[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}")) {
                binding.tilEmail.setError("Email không hợp lệ!");
                binding.tilEmail.requestFocus();
                return;
            } else {
                binding.tilEmail.setErrorEnabled(false);
            }

            binding.btnSendResetLink.setEnabled(false);
            binding.btnSendResetLink.setText("Đang gửi liên kết...");

            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(ForgotPasswordActivity.this, "Đã gửi liên kết khôi phục!", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(ForgotPasswordActivity.this, VerifyEmailActivity.class);
                    startActivity(intent);
                    finish();
                }
            }, 2000);



        });
    }
}