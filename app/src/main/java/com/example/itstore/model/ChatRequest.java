package com.example.itstore.model;

import java.util.List;

public class ChatRequest {
    private String message;
    private List<ChatHistory> history;

    public ChatRequest(String message, List<ChatHistory> history) {
        this.message = message;
        this.history = history;
    }

    public String getMessage() { return message; }
    public List<ChatHistory> getHistory() { return history; }
}