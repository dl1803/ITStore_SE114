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
import com.example.itstore.databinding.ActivityVerifyEmailBinding;

public class VerifyEmailActivity extends AppCompatActivity {
    private ActivityVerifyEmailBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityVerifyEmailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnLogin.setOnClickListener(v -> {
            Intent intent = new Intent(VerifyEmailActivity.this, LoginActivity.class);
            startActivity(intent);
        });


        binding.tvResendEmail.setOnClickListener(v -> {
            binding.tvResendEmail.setEnabled(false);
            binding.tvResendEmail.setText("Đang gửi lại...");

            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(VerifyEmailActivity.this, "Đã gửi lại email xác thực!", Toast.LENGTH_LONG).show();
                    binding.tvResendEmail.setEnabled(true);
                    binding.tvResendEmail.setText("Gửi lại email");
                }
            }, 2000);
        });

    }
}