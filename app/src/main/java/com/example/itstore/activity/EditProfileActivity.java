package com.example.itstore.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.itstore.R;
import com.example.itstore.databinding.ActivityEditProfileBinding;
import com.example.itstore.viewmodel.ProfileViewModel;

public class EditProfileActivity extends AppCompatActivity {
    private ActivityEditProfileBinding binding;
    private ProfileViewModel profileViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        profileViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);

        Intent intent = getIntent();
        binding.edtFullName.setText(intent.getStringExtra("name"));
        binding.edtPhone.setText(intent.getStringExtra("phone"));
        binding.edtEmail.setText(intent.getStringExtra("email"));

        binding.edtEmail.setEnabled(false);

        binding.imgBack.setOnClickListener(v -> finish());

        binding.btnSaveProfile.setOnClickListener(v -> {
            String newName = binding.edtFullName.getText().toString().trim();
            String newPhone = binding.edtPhone.getText().toString().trim();

            profileViewModel.updateProfile(newName, newPhone);
        });

        setupObservers();
    }

    private void setupObservers() {
        profileViewModel.getUpdateSuccessMessage().observe(this, msg -> {
            if (msg != null) {
                Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
                finish();
            }
        });
        profileViewModel.getErrorMessage().observe(this, error -> {
            if (error != null) {
                Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
            }
        });
        profileViewModel.getNameError().observe(this, error -> {
            if (error != null) {
                binding.edtFullName.setError(error);
                binding.edtFullName.requestFocus();
            } else {
                binding.edtFullName.setError(null);
            }
        });

        profileViewModel.getPhoneError().observe(this, error -> {
            if (error != null) {
                binding.edtPhone.setError(error);
                binding.edtPhone.requestFocus();
            } else {
                binding.edtPhone.setError(null);
            }
        });
    }
}