package com.example.itstore.model;

public class ChatMessage {
    private String text;
    private boolean isUser; // true: Tin nhắn của khách, false: Tin nhắn của AI

    public ChatMessage(String text, boolean isUser) {
        this.text = text;
        this.isUser = isUser;
    }

    public String getText() { return text; }
    public boolean isUser() { return isUser; }
}