package com.example.itstore.model;
import com.google.gson.annotations.SerializedName;

public class GhnResponse<T> {
    // Dùng Generic <T> (kiểu dữ liệu linh hoạt ) để tái sử dụng cho cả Tỉnh, Quận, Phường
    @SerializedName("code")
    private int code;
    @SerializedName("data")
    private T data;

    public int getCode() { return code; }
    public T getData() { return data; }
}