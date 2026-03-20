package com.example.itstore.activity;

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

        handleLink(getIntent());

        setupObservers();

        binding.btnSubmitReset.setOnClickListener(v -> {
            String newPass = binding.edtNewPassword.getText().toString().trim();
            String confirmPass = binding.edtConfirmPassword.getText().toString().trim();

            viewModel.resetPassword(resetToken, newPass, confirmPass);
        });

        binding.btnBack.setOnClickListener(v -> finish());
    }

    private void handleLink(Intent intent) {
        if (Intent.ACTION_VIEW.equals(intent.getAction())) {
            Uri uri = intent.getData();
            if (uri != null) {
                resetToken = uri.getQueryParameter("token");

                if (resetToken == null || resetToken.isEmpty()) {
                    Toast.makeText(this, "Link không hợp lệ hoặc đã hết hạn!", Toast.LENGTH_LONG).show();
                    finish();
                }
            }
        }
    }

    private void setupObservers() {
        viewModel.getPasswordError().observe(this, error -> {
            binding.tilNewPassword.setError(error);
        });

        viewModel.getConfirmPasswordError().observe(this, error -> {
            binding.tilConfirmPassword.setError(error);
        });

        viewModel.getApiError().observe(this, error -> {
            if (error != null) {
                Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
            }
        });

        viewModel.getSuccessMessage().observe(this, message -> {
            if (message != null) {
                Toast.makeText(this, message, Toast.LENGTH_LONG).show();

                Intent intent = new Intent(this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });

        viewModel.getIsLoading().observe(this, isLoading -> {
            binding.btnSubmitReset.setEnabled(!isLoading);
        });
    }
}