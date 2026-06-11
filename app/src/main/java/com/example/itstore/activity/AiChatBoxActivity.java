package com.example.itstore.activity;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.example.itstore.adapter.ChatAdapter;
import com.example.itstore.databinding.ActivityAiChatBoxBinding;
import com.example.itstore.viewmodel.AiChatViewModel;

public class AiChatBoxActivity extends AppCompatActivity {

    private ActivityAiChatBoxBinding binding;
    private ChatAdapter chatAdapter;
    private AiChatViewModel viewModel;

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

        viewModel = new ViewModelProvider(this).get(AiChatViewModel.class);

        chatAdapter = new ChatAdapter(new java.util.ArrayList<>());

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

        viewModel.getMessageList().observe(this, messages -> {
            chatAdapter = new ChatAdapter(messages);
            binding.rcvMessages.setAdapter(chatAdapter);
            if (!messages.isEmpty()) {
                binding.rcvMessages.scrollToPosition(messages.size() - 1);
            }
        });

        viewModel.getStreamUpdate().observe(this, update -> {
            if (update != null) {
                chatAdapter.streamToken(binding.rcvMessages, update.botIndex, update.token, update.fullText);
                binding.rcvMessages.scrollToPosition(update.botIndex);
            }
        });

        viewModel.getIsSending().observe(this, sending -> {
            binding.btnSend.setEnabled(!sending);
        });

        if (viewModel.getMessageList().getValue() == null || viewModel.getMessageList().getValue().isEmpty()) {
            viewModel.addMessage("Chào bạn! Mình là AI tư vấn của IT Store. Bạn cần mình giúp gì ạ?", false);
        }

        binding.imgClose.setOnClickListener(v -> finish());

        binding.btnSend.setOnClickListener(v -> handleSend());

        binding.edtMessage.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEND ||
                    (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN)) {
                handleSend();
                return true;
            }
            return false;
        });
    }

    private void handleSend() {
        String text = binding.edtMessage.getText().toString().trim();
        if (text.isEmpty()) return;
        if (text.length() > 500) {
            Toast.makeText(this, "Tin nhắn tối đa 500 ký tự", Toast.LENGTH_SHORT).show();
            return;
        }
        binding.edtMessage.setText("");
        viewModel.sendMessage(text);
    }
}