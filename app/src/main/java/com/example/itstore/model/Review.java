package com.example.itstore.model;

import java.io.Serializable;

public class Review implements Serializable { // implements Serializable để truyền dữ liệu phức tạp (đối tượng,..) qua intent
    private int id;
    private String userName;
    private float rating;
    private String comment;
    private long timestamp;

    private String dateString;

    public Review(int id, String userName, float rating, String comment, long timestamp, String dateString){
        this.id = id;
        this.userName = userName;
        this.rating = rating;
        this.comment = comment;
        this.timestamp = timestamp;
        this.dateString = dateString;
    }

    public int getId(){ return id; }
    public String getUserName(){ return userName; }
    public float getRating(){ return rating; }
    public String getComment(){ return comment; }
    public long getTimestamp(){ return timestamp; }
    public String getDateString(){ return dateString; }
}
