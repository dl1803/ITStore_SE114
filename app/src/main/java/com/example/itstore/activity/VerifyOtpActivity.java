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
import com.example.itstore.viewmodel.VerifyOtpViewModel;

public class VerifyOtpActivity extends AppCompatActivity {

    private ActivityVerifyOtpBinding binding;
    private String userEmail = "";
    private CountDownTimer timer;
    private boolean isTimerRunning = false;
    private VerifyOtpViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        binding = ActivityVerifyOtpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = new ViewModelProvider(this).get(VerifyOtpViewModel.class);

        Intent intent = getIntent();
        boolean isEmailFailed = false;
        if (intent != null) {
            if (intent.hasExtra("USER_EMAIL")) { userEmail = intent.getStringExtra("USER_EMAIL"); }
            isEmailFailed = intent.getBooleanExtra("EMAIL_FAILED", false);
        }

        setupObservers();

        if (isEmailFailed) {
            binding.tvSubtitle.setText("Hiện chưa gửi được mail. Vui lòng bấm 'Gửi lại mã'.");
            isTimerRunning = false;
            binding.tvResend.setEnabled(true);
            binding.tvResend.setText("Gửi lại mã");
            binding.tvResend.setTextColor(ContextCompat.getColor(this, R.color.orange_primary));
        } else {
            binding.tvSubtitle.setText("Vui lòng nhập mã xác thực gửi đến " + userEmail);
            startCountDownTimer();
        }

        binding.tvSubtitle.setText("Vui lòng nhập mã xác thực 6 ký tự đã được gửi đến " + userEmail);


        binding.btnBack.setOnClickListener(v -> finish());

        binding.btnVerify.setOnClickListener(v -> {
            String otp = binding.pinViewOtp.getText().toString();
            if (otp.length() < 6) {
                Toast.makeText(this, "Vui lòng nhập đủ 6 kí tự xác thực!", Toast.LENGTH_SHORT).show();
                viewModel.verifyOtp(userEmail, otp);
                return;
            }
            viewModel.verifyOtp(userEmail, otp);
        });

        binding.tvResend.setOnClickListener(v -> {
            if (!isTimerRunning) {
                viewModel.resendOtp(userEmail);
                startCountDownTimer();
            } else {
                Toast.makeText(this, "Vui lòng đợi hết thời gian để gửi lại mã!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupObservers() {
        viewModel.getIsVerifying().observe(this, isVerifying -> {
            binding.btnVerify.setEnabled(!isVerifying);
            binding.btnVerify.setText(isVerifying ? "Đang xác thực..." : "Xác nhận");
        });

        viewModel.getVerifySuccess().observe(this, message -> {
            if (message != null) {
                Toast.makeText(this, "Xác thực thành công!", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(VerifyOtpActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });

        viewModel.getVerifyError().observe(this, error -> {
            if (error != null) {
                Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
                binding.pinViewOtp.setText("");
                binding.pinViewOtp.requestFocus();
            }
        });

        viewModel.getResendSuccess().observe(this, message -> {
            if (message != null) {
                Toast.makeText(this, "Đã gửi lại mã xác thực!", Toast.LENGTH_SHORT).show();
                binding.tvSubtitle.setText("Vui lòng nhập mã xác thực gửi đến " + userEmail);
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
                binding.tvResend.setTextColor(ContextCompat.getColor(VerifyOtpActivity.this, R.color.orange_primary));
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