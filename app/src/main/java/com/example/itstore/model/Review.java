package com.example.itstore.model;

import java.io.Serializable;
import java.util.List;

public class Review implements Serializable {
    private int id;
    private String userName;
    private float rating;
    private String comment;
    private long timestamp;
    private String dateString;

    private List<String> imageUrls;

    public Review(int id, String userName, float rating, String comment, long timestamp, String dateString){
        this.id = id;
        this.userName = userName;
        this.rating = rating;
        this.comment = comment;
        this.timestamp = timestamp;
        this.dateString = dateString;
    }

    public Review(int id, String userName, float rating, String comment, long timestamp, String dateString, List<String> imageUrls){
        this.id = id;
        this.userName = userName;
        this.rating = rating;
        this.comment = comment;
        this.timestamp = timestamp;
        this.dateString = dateString;
        this.imageUrls = imageUrls;
    }

    public int getId(){ return id; }
    public String getUserName(){ return userName; }
    public float getRating(){ return rating; }
    public String getComment(){ return comment; }
    public long getTimestamp(){ return timestamp; }
    public String getDateString(){ return dateString; }


    public List<String> getImageUrls() { return imageUrls; }
    public void setImageUrls(List<String> imageUrls) { this.imageUrls = imageUrls; }

    public String getReadableDate() { return dateString; }
    public String getDate() { return dateString; }
}