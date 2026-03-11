package com.example.itstore.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Patterns;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.itstore.R;

import com.example.itstore.databinding.ActivityLoginBinding;
import com.example.itstore.utils.SharedPrefsManager;
import com.example.itstore.utils.SharedPrefsManager;
import com.example.itstore.viewmodel.LoginViewModel;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    private LoginViewModel loginViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);


        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        

        // Chuyển tới trang Đăng ký
        binding.tvRegister.setOnClickListener((v) -> {
            Intent intent = new Intent(this, RegisterActivity.class);
            startActivity(intent);
        });

        // Chuyển tới trang Quên mật khẩu
        binding.tvForgotPassword.setOnClickListener(v ->
        {
            Intent intent = new Intent(this, ForgotPasswordActivity.class);
            startActivity(intent);
        });

        // Đăng nhập bằng GG
        binding.btnGoogleLogin.setOnClickListener(v ->
        {
            Toast.makeText(this, "Tính năng Đăng nhập Google đang được phát triển!", Toast.LENGTH_SHORT).show();
        });


        // Lắng nghe và xử lí sự kiện khi nhập email và mật khẩu
        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);
        setupObservers();

        // Xử lí sự kiện khi nhấn nút Đăng nhập
        binding.btnLogin.setOnClickListener(v -> {
            String email = binding.edtEmail.getText().toString().trim();
            String passwd = binding.edtPassword.getText().toString().trim();

            // CHEAT CODE: Đăng nhập bằng quyền Dev
            if (email.equals("dev") && passwd.equals("123")) {
                SharedPrefsManager.getInstance(this).saveTokens("token_gia_cua_dev", "refresh_gia");
                Toast.makeText(this, "Đăng nhập bằng quyền Dev!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, MainActivity.class));
                finish();
                return;
            }

            loginViewModel.login(email, passwd);
        });
    }

    private void setupObservers() {
        loginViewModel.getEmailError().observe(this, errorMessage -> {
            if (errorMessage != null){
                binding.tilEmail.setError(errorMessage);
                binding.tilEmail.requestFocus();
            } else {
                binding.tilEmail.setErrorEnabled(false);
            }
        });


        loginViewModel.getPasswordError().observe(this, errorMessage -> {
            if (errorMessage != null){
                binding.tilPassword.setError(errorMessage);
                binding.tilPassword.requestFocus();
            } else {
                binding.tilPassword.setErrorEnabled(false);
            }
        }
        );

        loginViewModel.getIsLoading().observe(this, isLoading ->{
            if (isLoading) {
                binding.btnLogin.setEnabled(false);
                binding.btnLogin.setText("Đang đăng nhập...");
            } else {
                binding.btnLogin.setEnabled(true);
                binding.btnLogin.setText("Đăng nhập");
            }
        });

        loginViewModel.getApiError().observe(this, errorMessage -> {
            if (errorMessage != null) {
                Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
            }
        });

        loginViewModel.getLoginSuccessData().observe(this, data -> {
            if (data != null) {
                SharedPrefsManager.getInstance(this).saveTokens(
                        data.getAccessToken(),
                        data.getRefreshToken()
                );

                Toast.makeText(LoginActivity.this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}