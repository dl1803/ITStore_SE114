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

import com.example.itstore.R;

import com.example.itstore.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);


        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //Thông báo đã vào màn hình Đăng nhập
        Toast.makeText(this, "Đã vào màn hình Đăng nhập!", Toast.LENGTH_LONG).show();


        // Chuyển tới trang Đăng ký
        binding.tvRegister.setOnClickListener(v -> {
            android.content.Intent intent = new android.content.Intent(this, RegisterActivity.class);
            startActivity(intent);
        });

        setupLoginBtn();
    }


    // Check đăng nhập
    private void setupLoginBtn(){
        binding.btnLogin.setOnClickListener(v -> {
            String email = binding.edtEmail.getText().toString().trim();
            String passwd = binding.edtPassword.getText().toString().trim();

            if (email.isEmpty()) {
                binding.tilEmail.setError("Vui lòng nhập email");
                binding.tilEmail.requestFocus();
                return;
            } else if (!email.matches("[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}")) {
                binding.tilEmail.setError("Email không hợp lệ!");
                binding.tilEmail.requestFocus();
                return;
            } else {
                binding.tilEmail.setErrorEnabled(false);
            }


            if (passwd.isEmpty()) {
                binding.tilPassword.setError("Vui lòng nhập mật khẩu");
                binding.tilPassword.requestFocus();
                return;
            }  else if (passwd.length() < 8) {
                binding.tilPassword.setError("Mật khẩu phải có ít nhất 8 ký tự");
                binding.tilPassword.requestFocus();
                return;
            } else if (!passwd.matches(".*[a-z].*")) {
                binding.tilPassword.setError("Mật khẩu phải có ít nhất 1 ký tự thường");
                binding.tilPassword.requestFocus();
                return;
            } else if (!passwd.matches(".*[A-Z].*")) {
                binding.tilPassword.setError("Mật khẩu phải có ít nhất 1 ký tự in hoa");
                binding.tilPassword.requestFocus();
                return;
            } else if (!passwd.matches(".*[0-9].*")) {
                binding.tilPassword.setError("Mật khẩu phải có ít nhất 1 số");
                binding.tilPassword.requestFocus();
                return;
            } else if (!passwd.matches(".*[^a-zA-Z0-9].*")) {
                binding.tilPassword.setError("Mật khẩu phải có ít nhất 1 ký tự đặc biệt");
                binding.tilPassword.requestFocus();
                return;
            } else {
                binding.tilPassword.setErrorEnabled(false);
            }



            binding.btnLogin.setEnabled(false);
            binding.btnLogin.setText("Đang đăng nhập...");


            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(LoginActivity.this, "Đăng nhập thành công!", Toast.LENGTH_LONG).show();

                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }, 2000);
        });
    }
}