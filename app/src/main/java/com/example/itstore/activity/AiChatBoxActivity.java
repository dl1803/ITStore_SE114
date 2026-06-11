package com.example.itstore.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.example.itstore.adapter.ChatAdapter;
import com.example.itstore.databinding.ActivityAiChatBoxBinding;
import com.example.itstore.model.ChatHistory;
import com.example.itstore.model.ChatMessage;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AiChatBoxActivity extends AppCompatActivity {

    private ActivityAiChatBoxBinding binding;
    private List<ChatMessage> messageList;
    private List<ChatHistory> history;
    private ChatAdapter chatAdapter;
    private boolean isSending = false;

    private static final String STREAM_URL = "http://10.0.2.2:3000/api/chatbot/stream";

    private final OkHttpClient httpClient = new OkHttpClient.Builder()
            .readTimeout(60, TimeUnit.SECONDS)
            .connectTimeout(15, TimeUnit.SECONDS)
            .build();

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
        history = new ArrayList<>();
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

        addMessage("Chào bạn! Mình là AI tư vấn của IT Store. Bạn cần mình giúp gì ạ?", false);

        binding.imgClose.setOnClickListener(v -> finish());

        binding.btnSend.setOnClickListener(v -> sendMessage());

        binding.edtMessage.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEND ||
                    (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN)) {
                sendMessage();
                return true;
            }
            return false;
        });
    }

    private void sendMessage() {
        if (isSending) return;

        String text = binding.edtMessage.getText().toString().trim();
        if (text.isEmpty()) return;
        if (text.length() > 500) {
            Toast.makeText(this, "Tin nhắn tối đa 500 ký tự", Toast.LENGTH_SHORT).show();
            return;
        }

        binding.edtMessage.setText("");
        isSending = true;
        binding.btnSend.setEnabled(false);

        addMessage(text, true);

        messageList.add(new ChatMessage("", false));
        int botIndex = messageList.size() - 1;
        chatAdapter.notifyItemInserted(botIndex);
        binding.rcvMessages.scrollToPosition(botIndex);

        JSONObject body = new JSONObject();
        JSONArray historyArray = new JSONArray();
        try {
            for (ChatHistory h : history) {
                JSONObject item = new JSONObject();
                item.put("role", h.getRole());
                item.put("content", h.getContent());
                historyArray.put(item);
            }
            body.put("message", text);
            body.put("history", historyArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Request request = new Request.Builder()
                .url(STREAM_URL)
                .post(RequestBody.create(body.toString(), MediaType.parse("application/json; charset=utf-8")))
                .build();

        Handler mainHandler = new Handler(Looper.getMainLooper());
        StringBuilder fullReply = new StringBuilder();

        httpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                mainHandler.post(() -> {
                    messageList.set(botIndex, new ChatMessage("Không thể kết nối. Vui lòng kiểm tra mạng!", false));
                    chatAdapter.notifyItemChanged(botIndex);
                    isSending = false;
                    binding.btnSend.setEnabled(true);
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful() || response.body() == null) {
                    mainHandler.post(() -> {
                        messageList.set(botIndex, new ChatMessage("Xin lỗi, mình đang gặp sự cố. Thử lại sau nhé!", false));
                        chatAdapter.notifyItemChanged(botIndex);
                        isSending = false;
                        binding.btnSend.setEnabled(true);
                    });
                    return;
                }

                try (BufferedReader reader = new BufferedReader(new InputStreamReader(response.body().byteStream()))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        if (!line.startsWith("data: ")) continue;

                        String data = line.substring(6).trim();
                        if (data.equals("[DONE]")) break;

                        try {
                            JSONObject json = new JSONObject(data);
                            if (json.has("token")) {
                                fullReply.append(json.getString("token"));
                                String current = fullReply.toString();
                                String capturedToken = json.getString("token");
                                mainHandler.post(() -> {
                                    chatAdapter.streamToken(binding.rcvMessages, botIndex, capturedToken, current);
                                    binding.rcvMessages.scrollToPosition(botIndex);
                                });
                            }
                        } catch (JSONException ignored) {}
                    }
                }

                String finalReply = fullReply.toString();
                mainHandler.post(() -> {
                    if (!finalReply.isEmpty()) {
                        history.add(new ChatHistory("user", text));
                        history.add(new ChatHistory("assistant", finalReply));
                        if (history.size() > 20) {
                            history.subList(0, history.size() - 20).clear();
                        }
                    } else {
                        messageList.set(botIndex, new ChatMessage("Xin lỗi, mình không thể trả lời lúc này.", false));
                        chatAdapter.notifyItemChanged(botIndex);
                    }
                    isSending = false;
                    binding.btnSend.setEnabled(true);
                });
            }
        });
    }

    private void addMessage(String text, boolean isUser) {
        messageList.add(new ChatMessage(text, isUser));
        chatAdapter.notifyItemInserted(messageList.size() - 1);
        binding.rcvMessages.scrollToPosition(messageList.size() - 1);
    }
}