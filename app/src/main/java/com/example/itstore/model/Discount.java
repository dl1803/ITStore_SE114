package com.example.itstore.model;

public class Discount {
    private String code;
    private String title;
    private String condition;
    private String date;
    private double amount;

    public Discount(String code, String title, String condition, String date, double amount) {
        this.code = code;
        this.title = title;
        this.condition = condition;
        this.date = date;
        this.amount = amount;
    }
    public String getCode() { return code; }
    public String getTitle() { return title; }
    public String getCondition() { return condition; }
    public String getDate() { return date; }
    public double getAmount() { return amount; }
}
