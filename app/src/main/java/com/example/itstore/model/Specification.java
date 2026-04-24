package com.example.itstore.model;

public class Specification {
    private String key;
    private String value;

    public Specification(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() { return key; }
    public String getValue() { return value; }
}
