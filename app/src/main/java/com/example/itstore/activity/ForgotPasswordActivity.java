package com.example.itstore.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.itstore.databinding.ActivityForgotPasswordBinding;
import com.example.itstore.viewmodel.ForgotPasswordViewModel;

public class ForgotPasswordActivity extends AppCompatActivity {
    private ActivityForgotPasswordBinding binding;
    private ForgotPasswordViewModel forgotPasswordViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        binding = ActivityForgotPasswordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //Chuyển về màn hình đăng nhập
        binding.btnBack.setOnClickListener(view -> {
            finish();
        });

        // Lắng nghe và xử lí sự kiện khi nhập email
        forgotPasswordViewModel = new ViewModelProvider(this).get(ForgotPasswordViewModel.class);

        setupObservers();

        binding.btnSendResetLink.setOnClickListener(v -> {
            String email = binding.edtEmail.getText().toString().trim();
            forgotPasswordViewModel.sendResetLink(email);
        });
    }
    private void setupObservers() {
        forgotPasswordViewModel.getEmailError().observe(this, error -> {
            if (error != null) {
                binding.tilEmail.setError(error);
                binding.tilEmail.requestFocus();
            } else {
                binding.tilEmail.setErrorEnabled(false);
            }
        });

        forgotPasswordViewModel.getIsLoading().observe(this, isLoading -> {
            if (isLoading) {
                binding.btnSendResetLink.setEnabled(false);
                binding.btnSendResetLink.setText("Đang gửi liên kết...");
            } else {
                binding.btnSendResetLink.setEnabled(true);
                binding.btnSendResetLink.setText("Gửi liên kết khôi phục");
            }
        });

        forgotPasswordViewModel.getIsSuccess().observe(this, isSuccess -> {
            if (isSuccess) {
                Toast.makeText(this, "Đã gửi liên kết khôi phục!", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(this, VerifyEmailActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }
}