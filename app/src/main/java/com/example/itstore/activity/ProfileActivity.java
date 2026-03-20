package com.example.itstore.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.itstore.databinding.ActivityProfileBinding;
import com.example.itstore.utils.SharedPrefsManager;
import com.example.itstore.viewmodel.ProfileViewModel;

public class ProfileActivity extends AppCompatActivity {

    private ActivityProfileBinding binding;
    private ProfileViewModel profileViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        profileViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);

        setupObservers();

        String token = SharedPrefsManager.getInstance(this).getAccessToken();

        if (token == null || token.isEmpty()) {
            binding.tvNameUser.setText("Xin chào, Khách!");
            binding.tvEmailUser.setText("Vui lòng đăng nhập để xem hồ sơ");
            binding.tvPhoneUser.setText("");
            binding.tvRoleUser.setText("Guest");

            binding.tvLogout.setText("Đăng nhập");
            binding.tvLogout.setOnClickListener(v -> {
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
            });

        } else {

            String cachedName = SharedPrefsManager.getInstance(this).getUserFullName();
            String cachedEmail = SharedPrefsManager.getInstance(this).getUserEmail();


            binding.tvNameUser.setText(cachedName);
            binding.tvEmailUser.setText(cachedEmail);



            profileViewModel.fetchProfile();

            binding.tvLogout.setOnClickListener(v -> {
                profileViewModel.logout();
            });
        }
    }

    private void setupObservers() {
        profileViewModel.getUserProfile().observe(this, user -> {
            if (user != null) {
                binding.tvNameUser.setText(user.getFull_name());
                binding.tvEmailUser.setText(user.getEmail());
                binding.tvPhoneUser.setText(user.getPhone_number());
                binding.tvRoleUser.setText(user.getRole());

                if (user.getAvatar_url() != null && !user.getAvatar_url().isEmpty()) {

                    Glide.with(this)
                            .load(user.getAvatar_url())
                            .circleCrop()
                            .into(binding.imgAvatar);
                }
            }
        });

        profileViewModel.getErrorMessage().observe(this, error -> {
            if (error != null) {
                Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
            }
        });

        profileViewModel.getIsLogout().observe(this, isLogout -> {
            if (isLogout != null && isLogout) {
                SharedPrefsManager.getInstance(this).clear();
                Intent intent = new Intent(this , LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });
    }
}