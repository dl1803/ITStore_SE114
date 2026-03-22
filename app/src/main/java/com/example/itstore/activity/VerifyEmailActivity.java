package com.example.itstore.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
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
import com.example.itstore.databinding.ActivityVerifyEmailBinding;
import com.example.itstore.viewmodel.VerifyEmailViewModel;

public class VerifyEmailActivity extends AppCompatActivity {
    private ActivityVerifyEmailBinding binding;
    private VerifyEmailViewModel verifyEmailViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityVerifyEmailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Lắng nghe và xử lí sự kiện khi nhấn nút Gửi lại
        verifyEmailViewModel = new ViewModelProvider(this).get(VerifyEmailViewModel.class);

        setupObservers();

        binding.tvResendEmail.setOnClickListener(v -> {
            verifyEmailViewModel.resendEmail();
        });

        // Lắng nghe và xử lí sự kiện khi nhấn nút Đăng nhập
        binding.btnLogin.setOnClickListener(v -> {
            Intent intent = new Intent(VerifyEmailActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });

        handleEmailLink(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        handleEmailLink(intent);
    }

    private void handleEmailLink(Intent intent) {
        Uri data = intent.getData();

        if (data != null){
            String successParam = data.getQueryParameter("success");
            String messageParam = data.getQueryParameter("message");

            String displayMessage;
            if (messageParam != null) {
                displayMessage = messageParam;
            } else {
                displayMessage = "Có lỗi xảy ra, vui lòng thử lại!";
            }

            if (successParam != null && successParam.equals("true")) {

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Thành công");
                builder.setMessage(displayMessage);
                builder.setCancelable(false);
                builder.setPositiveButton("Đăng nhập ngay", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent loginIntent = new Intent(VerifyEmailActivity.this, LoginActivity.class);
                        loginIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(loginIntent);
                        finish();
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();

            } else {

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Lỗi xác thực");
                builder.setMessage(displayMessage);

                builder.setPositiveButton("Đóng", null);

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        }
    }

    private void setupObservers() {
        verifyEmailViewModel.getIsResending().observe(this, isResending -> {
            if (isResending) {
                binding.tvResendEmail.setEnabled(false);
                binding.tvResendEmail.setText("Đang gửi lại...");
            } else {
                binding.tvResendEmail.setEnabled(true);
                binding.tvResendEmail.setText("Gửi lại email");
            }
        });

        verifyEmailViewModel.getIsResendSuccess().observe(this, isSuccess -> {
            if (isSuccess) {
                Toast.makeText(this, "Đã gửi lại email xác thực!", Toast.LENGTH_LONG).show();
            }
        });
    }
}