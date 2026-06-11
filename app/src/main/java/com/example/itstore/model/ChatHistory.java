package com.example.itstore.model;

public class ChatHistory {
    private String role;
    private String content;

    public ChatHistory(String role, String content) {
        this.role = role;
        this.content = content;
    }

    public String getRole() { return role; }
    public String getContent() { return content; }
}