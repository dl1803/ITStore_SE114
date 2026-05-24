package com.example.itstore.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.itstore.databinding.ActivityResetPasswordBinding;
import com.example.itstore.viewmodel.ResetPasswordViewModel;

public class ResetPasswordActivity extends AppCompatActivity {

    private ActivityResetPasswordBinding binding;
    private ResetPasswordViewModel viewModel;
    private String resetToken = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityResetPasswordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = new ViewModelProvider(this).get(ResetPasswordViewModel.class);

        if (getIntent() != null && getIntent().hasExtra("RESET_TOKEN")) {
            resetToken = getIntent().getStringExtra("RESET_TOKEN");
        }

        setupObservers();

        binding.btnSubmitReset.setOnClickListener(v -> {
            String newPass = binding.edtNewPassword.getText().toString().trim();
            String confirmPass = binding.edtConfirmPassword.getText().toString().trim();

            viewModel.resetPassword(resetToken, newPass, confirmPass);
        });

        binding.btnBack.setOnClickListener(v -> finish());
    }
    private void setupObservers() {
        viewModel.getPasswordError().observe(this, error -> {
            if (error != null) {
                binding.tilNewPassword.setError(error);
                binding.tilNewPassword.requestFocus();
            } else {
                binding.tilNewPassword.setErrorEnabled(false);
            }
        });

        viewModel.getConfirmPasswordError().observe(this, error -> {
            if (error != null) {
                binding.tilConfirmPassword.setError(error);
                binding.tilConfirmPassword.requestFocus();
            } else {
                binding.tilConfirmPassword.setErrorEnabled(false);
            }
        });

        viewModel.getApiError().observe(this, error -> {
            if (error != null) {
                Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
            }
        });

        viewModel.getSuccessMessage().observe(this, message -> {
            if (message != null) {
                new AlertDialog.Builder(this)
                        .setTitle("Thành công")
                        .setMessage(message)
                        .setPositiveButton("Đăng nhập ngay", (dialog, which) -> {
                            Intent intent = new Intent(ResetPasswordActivity.this, LoginActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();
                        })
                        .setCancelable(false)
                        .show();
            }
        });

        viewModel.getIsLoading().observe(this, isLoading -> {
            binding.btnSubmitReset.setEnabled(!isLoading);
            if (isLoading) {
                binding.btnSubmitReset.setText("Đang xử lý...");
            } else {
                binding.btnSubmitReset.setText("Cập nhật mật khẩu");
            }
        });
    }
}