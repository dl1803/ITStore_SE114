package com.example.itstore.model;

import com.google.gson.annotations.SerializedName;

public class User {
    private int id;

    private String full_name;
    private String email;
    private String phone_number;

    @SerializedName("avatar_url")
    private String avatar_url;
    private String role;

    public int getId() {
        return id;
    }

    public String getFull_name(){
        return full_name;
    }

    public String getEmail(){
        return email;
    }

    public String getPhone_number(){
        return phone_number;
    }

    public String getAvatar_url(){
        return avatar_url;
    }

    public String getRole(){
        return role;
    }

    public User(){
    }

    public User(int id, String full_name, String email, String phone_number, String role) {
        this.id = id;
        this.full_name = full_name;
        this.email = email;
        this.phone_number = phone_number;
        this.role = role;
    }


}
