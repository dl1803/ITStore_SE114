package com.example.itstore.activity;

import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.itstore.R;
import com.example.itstore.databinding.ActivityChangePasswordBinding;
import com.example.itstore.viewmodel.ProfileViewModel;

public class ChangePasswordActivity extends AppCompatActivity {

    private ActivityChangePasswordBinding binding;
    private ProfileViewModel profileViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        binding = ActivityChangePasswordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        profileViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);

        binding.btnBack.setOnClickListener(v -> finish());

        binding.btnSavePassword.setOnClickListener(v -> {
            String oldPassword = binding.edtOldPassword.getText().toString();
            String newPassword = binding.edtNewPassword.getText().toString();
            String confirmPassword = binding.edtConfirmPassword.getText().toString();

            profileViewModel.changePassword(oldPassword, newPassword, confirmPassword);
        });

        setupObservers();
    }

    public void setupObservers() {
        profileViewModel.getChangeSuccessMessage().observe(this, message -> {
            if (message != null){
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        profileViewModel.getChangeErrorMessage().observe(this, error -> {
            if (error != null){
                Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
            }
        });

        profileViewModel.getOldPasswordError().observe(this, error -> {
            if (error != null) {
                binding.tilOldPassword.setError(error);
                binding.edtOldPassword.requestFocus();
            } else {
                binding.tilOldPassword.setError(null);
            }
        });

        profileViewModel.getNewPasswordError().observe(this, error -> {
            if (error != null) {
                if (error.contains("khớp")) {
                    binding.tilConfirmPassword.setError(error);
                    binding.tilNewPassword.setError(null);
                    binding.edtConfirmPassword.requestFocus();
                } else {
                    binding.tilNewPassword.setError(error);
                    binding.tilConfirmPassword.setError(null);
                    binding.edtNewPassword.requestFocus();
                }
            } else {
                binding.tilNewPassword.setError(null);
                binding.tilConfirmPassword.setError(null);
            }
        });


    }
}