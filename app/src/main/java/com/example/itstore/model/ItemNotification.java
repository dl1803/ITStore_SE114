package com.example.itstore.model;

public class ItemNotification {
    private String id;
    private String title;
    private String content;
    private String time;
    private boolean isRead;
    public ItemNotification(String id, String title, String content, String time, boolean isRead) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.time = time;
        this.isRead = isRead;
    }

    public String getId() { return id; }
    public String getTitle() { return title; }
    public String getContent() { return content; }
    public String getTime() { return time; }
    public boolean isRead() { return isRead; }

    // Hàm này dùng để cập nhật lại trạng thái khi User bấm vào tin nhắn
    public void setRead(boolean read) { isRead = read; }
}
