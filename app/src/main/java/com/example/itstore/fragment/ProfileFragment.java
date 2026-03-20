package com.example.itstore.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.itstore.activity.LoginActivity;
import com.example.itstore.databinding.FragmentProfileBinding;
import com.example.itstore.utils.SharedPrefsManager;
import com.example.itstore.viewmodel.ProfileViewModel;

public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;
    private ProfileViewModel profileViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        profileViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
        setupObservers();

        String token = SharedPrefsManager.getInstance(requireContext()).getAccessToken();

        if (token == null || token.isEmpty()) {
            binding.tvNameUser.setText("Xin chào, Khách!");
            binding.tvEmailUser.setText("Vui lòng đăng nhập để xem hồ sơ");
            binding.tvPhoneUser.setText("");
            binding.tvRoleUser.setText("Guest");

            binding.tvLogout.setText("Đăng nhập");
            binding.tvLogout.setOnClickListener(v -> {
                Intent intent = new Intent(requireContext(), LoginActivity.class);
                startActivity(intent);
            });
        } else {
            String cachedName = SharedPrefsManager.getInstance(requireContext()).getUserFullName();
            String cachedEmail = SharedPrefsManager.getInstance(requireContext()).getUserEmail();

            binding.tvNameUser.setText(cachedName);
            binding.tvEmailUser.setText(cachedEmail);

            profileViewModel.fetchProfile();

            binding.tvLogout.setOnClickListener(v -> {
                profileViewModel.logout();
            });
        }

        return view;
    }

    private void setupObservers() {
        profileViewModel.getUserProfile().observe(getViewLifecycleOwner(), user -> {
            if (user != null) {
                binding.tvNameUser.setText(user.getFull_name());
                binding.tvEmailUser.setText(user.getEmail());
                binding.tvPhoneUser.setText(user.getPhone_number());
                binding.tvRoleUser.setText(user.getRole());

                if (user.getAvatar_url() != null && !user.getAvatar_url().isEmpty()) {
                    Glide.with(requireContext())
                            .load(user.getAvatar_url())
                            .circleCrop()
                            .into(binding.imgAvatar);
                }
            }
        });

        profileViewModel.getErrorMessage().observe(getViewLifecycleOwner(), error -> {
            if (error != null) {
                Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show();
            }
        });

        profileViewModel.getIsLogout().observe(getViewLifecycleOwner(), isLogout -> {
            if (isLogout != null && isLogout) {
                SharedPrefsManager.getInstance(requireContext()).clear();
                Intent intent = new Intent(requireContext(), LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                requireActivity().finish();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}