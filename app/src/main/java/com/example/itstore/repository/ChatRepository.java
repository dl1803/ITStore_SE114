package com.example.itstore.repository;

import android.os.Handler;
import android.os.Looper;
import com.example.itstore.model.ChatHistory;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.concurrent.TimeUnit;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ChatRepository {

    private static final String STREAM_URL = "http://10.0.2.2:3000/api/chatbot/stream";

    private final OkHttpClient httpClient = new OkHttpClient.Builder()
            .readTimeout(60, TimeUnit.SECONDS)
            .connectTimeout(15, TimeUnit.SECONDS)
            .build();

    public interface StreamCallback {
        void onToken(String token, String fullText);
        void onComplete(String fullReply);
        void onError(String errorMessage);
    }

    public void sendMessage(String text, List<ChatHistory> history, StreamCallback callback) {
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
                mainHandler.post(() -> callback.onError("Không thể kết nối. Vui lòng kiểm tra mạng!"));
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful() || response.body() == null) {
                    mainHandler.post(() -> callback.onError("Xin lỗi, mình đang gặp sự cố. Thử lại sau nhé!"));
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
                                String token = json.getString("token");
                                fullReply.append(token);
                                String current = fullReply.toString();
                                mainHandler.post(() -> callback.onToken(token, current));
                            }
                        } catch (JSONException ignored) {}
                    }
                }

                String finalReply = fullReply.toString();
                mainHandler.post(() -> callback.onComplete(finalReply));
            }
        });
    }
}