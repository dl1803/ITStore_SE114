package com.example.itstore.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.itstore.R;

import com.example.itstore.databinding.ActivityRegisterBinding;
import com.example.itstore.viewmodel.RegisterViewModel;

public class RegisterActivity extends AppCompatActivity {

    private ActivityRegisterBinding binding;
    private RegisterViewModel registerViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Chuyển về trang Đăng nhập khi nhấn vào "Đăng nhập"
        binding.tvLoginNow.setOnClickListener(v -> {
            finish();
        });

        // Chuyển về trang Đăng nhập khi nhấn vào nút Back
        binding.btnBack.setOnClickListener(v -> {
            finish();
        });

        // Lắng nghe và xử lí sự kiện khi nhập info
        registerViewModel = new ViewModelProvider(this).get(RegisterViewModel.class);
        setupObservers();

        // Xử lí sự kiện khi nhấn nút Đăng kí
        binding.btnRegister.setOnClickListener(v -> {
            String fullName = binding.edtFullName.getText().toString().trim();
            String email = binding.edtEmail.getText().toString().trim();
            String phone = binding.edtPhone.getText().toString().trim();
            String passwd = binding.edtPassword.getText().toString().trim();
            String confirmPasswd = binding.edtConfirmPassword.getText().toString().trim();

            registerViewModel.register(fullName, email, phone, passwd, confirmPasswd);
        });

    }

    private void setupObservers() {

        registerViewModel.getFullNameError().observe(this, errorMessage -> {
            if (errorMessage != null) {
                binding.tilFullName.setError(errorMessage);
                binding.tilFullName.requestFocus();
            } else {
                binding.tilFullName.setErrorEnabled(false);
            }
        });

        registerViewModel.getEmailError().observe(this, errorMessage -> {
            if (errorMessage != null) {
                binding.tilEmail.setError(errorMessage);
                binding.tilEmail.requestFocus();
            } else {
                binding.tilEmail.setErrorEnabled(false);
            }
        });

        registerViewModel.getPhoneError().observe(this, errorMessage -> {
            if (errorMessage != null) {
                binding.tilPhone.setError(errorMessage);
                binding.tilPhone.requestFocus();
            } else {
                binding.tilPhone.setErrorEnabled(false);
            }
        });

        registerViewModel.getPasswordError().observe(this, errorMessage -> {
            if (errorMessage != null) {
                binding.tilPassword.setError(errorMessage);
                binding.tilPassword.setErrorIconDrawable(null);
                binding.tilPassword.requestFocus();
            } else {
                binding.tilPassword.setErrorEnabled(false);
            }
        });

        registerViewModel.getConfirmPasswordError().observe(this, errorMessage -> {
            if (errorMessage != null) {
                binding.tilConfirmPassword.setError(errorMessage);
                binding.tilConfirmPassword.setErrorIconDrawable(null);
                binding.tilConfirmPassword.requestFocus();
            } else {
                binding.tilConfirmPassword.setErrorEnabled(false);
            }
        });

        registerViewModel.getIsLoading().observe(this, isLoading -> {
            if (isLoading){
                binding.btnRegister.setEnabled(false);
                binding.btnRegister.setText("Đang đăng kí...");
            } else {
                binding.btnRegister.setEnabled(true);
                binding.btnRegister.setText("Đăng kí");
            }
        });

        registerViewModel.getApiError().observe(this, errorMessage -> {
            if (errorMessage != null) {
                Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
            }
        });

        registerViewModel.getRegisterSuccessMessage().observe(this, message -> {
            if (message != null) {
                Toast.makeText(this, message, Toast.LENGTH_LONG).show();
                Intent intent = new Intent(RegisterActivity.this, VerifyEmailActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }
}

//}