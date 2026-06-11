package com.example.itstore.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.example.itstore.model.ChatHistory;
import com.example.itstore.model.ChatMessage;
import com.example.itstore.repository.ChatRepository;
import java.util.ArrayList;
import java.util.List;

public class AiChatViewModel extends ViewModel {

    private final ChatRepository repository = new ChatRepository();

    private final MutableLiveData<List<ChatMessage>> messageList = new MutableLiveData<>(new ArrayList<>());
    private final MutableLiveData<Boolean> isSending = new MutableLiveData<>(false);
    private final MutableLiveData<StreamUpdate> streamUpdate = new MutableLiveData<>();
    private final MutableLiveData<String> errorEvent = new MutableLiveData<>();

    private final List<ChatHistory> history = new ArrayList<>();

    public static class StreamUpdate {
        public final int botIndex;
        public final String token;
        public final String fullText;

        public StreamUpdate(int botIndex, String token, String fullText) {
            this.botIndex = botIndex;
            this.token = token;
            this.fullText = fullText;
        }
    }

    public LiveData<List<ChatMessage>> getMessageList() { return messageList; }
    public LiveData<Boolean> getIsSending() { return isSending; }
    public LiveData<StreamUpdate> getStreamUpdate() { return streamUpdate; }
    public LiveData<String> getErrorEvent() { return errorEvent; }

    public void addMessage(String text, boolean isUser) {
        List<ChatMessage> current = messageList.getValue();
        if (current == null) current = new ArrayList<>();
        current.add(new ChatMessage(text, isUser));
        messageList.setValue(current);
    }

    public void sendMessage(String text) {
        if (Boolean.TRUE.equals(isSending.getValue())) return;

        isSending.setValue(true);
        addMessage(text, true);

        List<ChatMessage> current = messageList.getValue();
        if (current == null) current = new ArrayList<>();
        current.add(new ChatMessage("", false));
        messageList.setValue(current);
        int botIndex = current.size() - 1;

        repository.sendMessage(text, history, new ChatRepository.StreamCallback() {
            @Override
            public void onToken(String token, String fullText) {
                List<ChatMessage> list = messageList.getValue();
                if (list != null && botIndex < list.size()) {
                    list.set(botIndex, new ChatMessage(fullText, false));
                    messageList.setValue(list);
                }
                streamUpdate.setValue(new StreamUpdate(botIndex, token, fullText));
            }

            @Override
            public void onComplete(String fullReply) {
                if (!fullReply.isEmpty()) {
                    history.add(new ChatHistory("user", text));
                    history.add(new ChatHistory("assistant", fullReply));
                    if (history.size() > 20) {
                        history.subList(0, history.size() - 20).clear();
                    }
                } else {
                    List<ChatMessage> list = messageList.getValue();
                    if (list != null && botIndex < list.size()) {
                        list.set(botIndex, new ChatMessage("Xin lỗi, mình không thể trả lời lúc này.", false));
                        messageList.setValue(list);
                    }
                }
                isSending.setValue(false);
            }

            @Override
            public void onError(String errorMessage) {
                List<ChatMessage> list = messageList.getValue();
                if (list != null && botIndex < list.size()) {
                    list.set(botIndex, new ChatMessage(errorMessage, false));
                    messageList.setValue(list);
                }
                isSending.setValue(false);
            }
        });
    }
}