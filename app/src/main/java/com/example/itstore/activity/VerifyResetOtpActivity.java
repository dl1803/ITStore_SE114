package com.example.itstore.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.itstore.R;
import com.example.itstore.databinding.ActivityVerifyOtpBinding;
import com.example.itstore.viewmodel.VerifyResetOtpViewModel;

public class VerifyResetOtpActivity extends AppCompatActivity {
    private ActivityVerifyOtpBinding binding;
    private VerifyResetOtpViewModel viewModel;
    private String userEmail = "";

    private CountDownTimer timer;
    private boolean isTimerRunning = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        binding = ActivityVerifyOtpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = new ViewModelProvider(this).get(VerifyResetOtpViewModel.class);

        if(getIntent() != null && getIntent().hasExtra("USER_EMAIL")) {
            userEmail = getIntent().getStringExtra("USER_EMAIL");
        }

        binding.tvSubtitle.setText("Vui lòng nhập mã đặt lại mật khẩu đã gửi đến " + userEmail);
        startCountDownTimer();

        setupObservers();

        binding.btnBack.setOnClickListener(v -> finish());

        binding.btnVerify.setOnClickListener(v -> {
            String code = binding.pinViewOtp.getText().toString();
            if (code.length() < 6) {
                Toast.makeText(this, "Vui lòng nhập đủ 6 ký tự!", Toast.LENGTH_SHORT).show();
                return;
            }
            viewModel.verifyCode(userEmail, code);
        });

        binding.tvResend.setOnClickListener(v -> {
            if (!isTimerRunning) {
                viewModel.resendCode(userEmail);
                startCountDownTimer();
            } else {
                Toast.makeText(this, "Vui lòng đợi hết thời gian để gửi lại!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupObservers() {
        viewModel.getIsLoading().observe(this, isLoading -> {
            binding.btnVerify.setEnabled(!isLoading);
            binding.btnVerify.setText(isLoading ? "Đang xác thực..." : "Xác nhận");
        });

        viewModel.getApiError().observe(this, error -> {
            if (error != null) {
                Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
                binding.pinViewOtp.setText("");
                binding.pinViewOtp.requestFocus();
            }
        });

        viewModel.getTokenSuccessData().observe(this, token -> {
            if (token != null) {
                Intent intent = new Intent(VerifyResetOtpActivity.this, ResetPasswordActivity.class);
                intent.putExtra("RESET_TOKEN", token);
                startActivity(intent);
                finish();
            }
        });
        viewModel.getResendSuccess().observe(this, message -> {
            if (message != null) {
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
            }
        });

        viewModel.getResendError().observe(this, error -> {
            if (error != null) { Toast.makeText(this, error, Toast.LENGTH_SHORT).show(); }
        });
    }

    private void startCountDownTimer() {
        isTimerRunning = true;
        binding.tvResend.setEnabled(false);
        binding.tvResend.setTextColor(android.graphics.Color.parseColor("#9E9E9E"));

        if (timer != null) timer.cancel();

        timer = new CountDownTimer(60000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                binding.tvResend.setText("Gửi lại (" + millisUntilFinished / 1000 + "s)");
            }
            @Override
            public void onFinish() {
                isTimerRunning = false;
                binding.tvResend.setText("Gửi lại mã");
                binding.tvResend.setTextColor(ContextCompat.getColor(VerifyResetOtpActivity.this, R.color.orange_primary));
                binding.tvResend.setEnabled(true);
            }
        }.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (timer != null) timer.cancel();
    }
}