package com.example.itstore.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.itstore.activity.AddressActivity;
import com.example.itstore.activity.ChangePasswordActivity;
import com.example.itstore.activity.EditProfileActivity;
import com.example.itstore.activity.LoginActivity;
import com.example.itstore.databinding.FragmentProfileBinding;
import com.example.itstore.utils.SharedPrefsManager;
import com.example.itstore.viewmodel.ProfileViewModel;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;
    private ProfileViewModel profileViewModel;


    // Chọn ảnh từ thư viện
    private final ActivityResultLauncher<String> pickImgLauncher = registerForActivityResult(
            new ActivityResultContracts.GetContent(), uri -> {
        if (uri != null){
            binding.imgAvatar.setImageURI(uri);
            File fileToUpload = convertUriToFile(uri);

            if (fileToUpload != null){
                profileViewModel.uploadAvatar(fileToUpload);
            } else {
                Toast.makeText(requireContext(), "Lỗi khi đọc file ảnh!", Toast.LENGTH_SHORT).show();
            }
        }
    });



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

            binding.tvEditProfile.setOnClickListener(v -> {
                Toast.makeText(requireContext(), "Bạn cần đăng nhập để sử dụng tính năng này!", Toast.LENGTH_SHORT).show();

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

            binding.tvEditProfile.setOnClickListener(v -> {
                Intent intent = new Intent(requireContext(), EditProfileActivity.class);
                intent.putExtra("name", binding.tvNameUser.getText().toString());
                intent.putExtra("phone", binding.tvPhoneUser.getText().toString());
                intent.putExtra("email", binding.tvEmailUser.getText().toString());
                startActivity(intent);
            });

            binding.tvChangePass.setOnClickListener(v -> {
                Intent intent = new Intent(requireContext(), ChangePasswordActivity.class);
                startActivity(intent);
            });

            binding.imgAvatar.setOnClickListener(v -> {
                pickImgLauncher.launch("image/*");
            });

            binding.tvAddressInfo.setOnClickListener(v -> {
                Intent intent = new Intent(requireContext(), AddressActivity.class);
                startActivity(intent);
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
                            .placeholder(android.R.drawable.ic_menu_camera)
                            .error(android.R.drawable.ic_menu_camera)
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

        profileViewModel.getAvatarUpdateStatus().observe(getViewLifecycleOwner(), status ->{
            if (status != null) {
                Toast.makeText(requireContext(), status, Toast.LENGTH_SHORT).show();
            }
        });
    }


    // Chuyển đổi Uri thành File
    private File convertUriToFile(Uri uri) {
        try {
            InputStream inputStream = requireContext().getContentResolver().openInputStream(uri);

            File tempFile = File.createTempFile("avatar_tmp", ".jpg", requireContext().getCacheDir());

            FileOutputStream outputStream = new FileOutputStream(tempFile);

            byte[] buffer = new byte[4 * 1024];
            int length;

            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }

            outputStream.close();
            inputStream.close();

            return tempFile;
        } catch (Exception e){
            return null;
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}