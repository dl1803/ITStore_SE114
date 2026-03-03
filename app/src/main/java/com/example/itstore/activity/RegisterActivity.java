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

import com.example.itstore.R;

import com.example.itstore.databinding.ActivityRegisterBinding;

public class RegisterActivity extends AppCompatActivity {

    private ActivityRegisterBinding binding;

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

        setupRegisterBtn();
    }

    //Check đăng ký
    private void setupRegisterBtn(){
        binding.btnRegister.setOnClickListener(v -> {

            String fullName = binding.edtFullName.getText().toString().trim();
            String email = binding.edtEmail.getText().toString().trim();
            String phone = binding.edtPhone.getText().toString().trim();
            String passwd = binding.edtPassword.getText().toString().trim();
            String confirmPasswd = binding.edtConfirmPassword.getText().toString().trim();


            if (fullName.isEmpty()){
                binding.tilFullName.setError("Vui lòng nhập họ và tên");
                binding.tilFullName.requestFocus();
                return;
            } else {
                binding.tilFullName.setErrorEnabled(false);
            }

            if (email.isEmpty()) {
                binding.tilEmail.setError("Vui lòng nhập email");
                binding.tilEmail.requestFocus();
                return;
            } else if (!email.matches("[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}")) {
                binding.tilEmail.setError("Email không hợp lệ! Vui lòng nhập lại");
                binding.tilEmail.requestFocus();
                return;
            } else {
                binding.tilEmail.setErrorEnabled(false);
            }


            if (phone.isEmpty()) {
                binding.tilPhone.setError("Vui lòng nhập số điện thoại");
                binding.tilPhone.requestFocus();
                return;
            } else if (!phone.matches("^(03|05|07|08|09)\\d{8}$")) {
                binding.tilPhone.setError("SĐT không hợp lệ!");
                binding.tilPhone.requestFocus();
                return;
            } else {
                binding.tilPhone.setErrorEnabled(false);
            }



            if (passwd.isEmpty()) {
                binding.tilPassword.setError("Vui lòng nhập mật khẩu");
                binding.tilPassword.requestFocus();
                return;
            } else if (passwd.length() < 8) {
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


            if (confirmPasswd.isEmpty()) {
                binding.tilConfirmPassword.setError("Vui lòng nhập xác nhận mật khẩu");
                binding.tilConfirmPassword.requestFocus();
                return;
            } else if (!passwd.equals(confirmPasswd)) {
                binding.tilConfirmPassword.setError("Mật khẩu không khớp!");
                binding.tilConfirmPassword.requestFocus();
                return;
            } else {
                binding.tilConfirmPassword.setErrorEnabled(false);
            }



            binding.btnRegister.setEnabled(false);
            binding.btnRegister.setText("Đang đăng kí...");


            Toast.makeText(RegisterActivity.this, "Đăng kí thành công! Vui lòng đăng nhập", Toast.LENGTH_LONG).show();

            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            }, 2000);

        });
    }
}