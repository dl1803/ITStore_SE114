package com.example.itstore.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.itstore.R;
import com.example.itstore.adapter.ChatAdapter;
import com.example.itstore.databinding.ActivityAiChatBoxBinding;
import com.example.itstore.model.ChatMessage;

import java.util.ArrayList;
import java.util.List;

public class AiChatBoxActivity extends AppCompatActivity {
    private ActivityAiChatBoxBinding binding;
    private List<ChatMessage> messageList;
    private ChatAdapter chatAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        binding = ActivityAiChatBoxBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ViewCompat.setOnApplyWindowInsetsListener(binding.main, (v, insets) -> {
            Insets systemBarsAndIme = insets.getInsets(WindowInsetsCompat.Type.systemBars() | WindowInsetsCompat.Type.ime());
            v.setPadding(systemBarsAndIme.left, systemBarsAndIme.top, systemBarsAndIme.right, systemBarsAndIme.bottom);
            return insets;
        });


        messageList = new ArrayList<>();
        chatAdapter = new ChatAdapter(messageList);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true);
        binding.rcvMessages.setLayoutManager(layoutManager);
        binding.rcvMessages.setAdapter(chatAdapter);

        binding.rcvMessages.addOnLayoutChangeListener((v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom) -> {
            if (bottom < oldBottom && chatAdapter.getItemCount() > 0) {
                binding.rcvMessages.postDelayed(() ->
                        binding.rcvMessages.smoothScrollToPosition(chatAdapter.getItemCount() - 1), 100);
            }
        });

        addMessageToChat("Chào bạn! Mình là AI tư vấn của IT Store. Bạn cần mình giúp gì ạ?", false);

        binding.imgClose.setOnClickListener(v -> finish());

        binding.btnSend.setOnClickListener(v -> {
            String question = binding.edtMessage.getText().toString().trim();

            if (!question.isEmpty()) {
                addMessageToChat(question, true);

                binding.edtMessage.setText("");

                binding.btnSend.setEnabled(false);

                // Tạm thời dùng Handler delay 1.5 giây để giả lập AI đang suy nghĩ
                new Handler(Looper.getMainLooper()).postDelayed(() -> {
                    addMessageToChat("Test Done!", false);
                    binding.btnSend.setEnabled(true);
                }, 1500);
            }
        });
    }

    private void addMessageToChat(String text, boolean isUser) {
        messageList.add(new ChatMessage(text, isUser));
        chatAdapter.notifyItemInserted(messageList.size() - 1);
        binding.rcvMessages.scrollToPosition(messageList.size() - 1);
    }
}