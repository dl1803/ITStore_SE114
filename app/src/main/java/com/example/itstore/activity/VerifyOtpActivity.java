package com.example.itstore.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.itstore.R;
import com.example.itstore.databinding.ActivityVerifyOtpBinding;

public class VerifyOtpActivity extends AppCompatActivity {

    private ActivityVerifyOtpBinding binding;
    private String userEmail = "";
    private CountDownTimer timer;
    private boolean isTimerRunning = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        binding = ActivityVerifyOtpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("USER_EMAIL")) {
            userEmail = intent.getStringExtra("USER_EMAIL");
        }

        binding.tvSubtitle.setText("Vui lòng nhập mã xác thực 6 ký tự đã được gửi đến " + userEmail);

        startCountDownTimer();

        binding.btnBack.setOnClickListener(v -> finish());

        binding.btnVerify.setOnClickListener(v -> {
            String otp = binding.pinViewOtp.getText().toString();
            if (otp.length() < 6) {
                Toast.makeText(this, "Vui lòng nhập đủ 6 kí tự xác thực!", Toast.LENGTH_SHORT).show();
                return;
            }

            verifyOtpApi(userEmail, otp);
        });

        binding.tvResend.setOnClickListener(v -> {
            if (!isTimerRunning) {
                resendOtpApi(userEmail);
                startCountDownTimer();
            } else {
                Toast.makeText(this, "Vui lòng đợi hết thời gian để gửi lại mã!", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void verifyOtpApi(String userEmail,String otpCode){
        binding.btnVerify.setEnabled(false);
        binding.btnVerify.setText("Đang xác thực...");
    }

    private void resendOtpApi(String email) {
        binding.tvResend.setEnabled(false);
        binding.tvResend.setText("Đang gửi lại...");
    }

    private void startCountDownTimer() {
        isTimerRunning = true;
        binding.tvResend.setTextColor(android.graphics.Color.parseColor("#9E9E9E"));

         timer = new CountDownTimer(60000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                binding.tvResend.setText("Gửi lại (" + millisUntilFinished / 1000 + "s)");
            }

            @Override
            public void onFinish() {
                isTimerRunning = false;
                binding.tvResend.setText("Gửi lại mã");
                binding.tvResend.setTextColor(ContextCompat.getColor(VerifyOtpActivity.this, R.color.orange_primary));

            }
        }.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (timer != null) timer.cancel();
    }
}