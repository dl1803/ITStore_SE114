package com.example.itstore.model;
import com.google.gson.annotations.SerializedName;

public class GhnProvince {
    @SerializedName("ProvinceID")
    private int provinceID;

    @SerializedName("ProvinceName")
    private String provinceName;

    public int getProvinceID() { return provinceID; }
    public String getProvinceName() { return provinceName; }

    // Vì dùng Adapter truyển vào list nên kh thể dùng getName để lấy tên item được
    // khi in list ra thì nó sẽ tự render in item.ToString()
    // khi đó cần ghi đề để ra đúng name của item , nếu kh sẽ hiển thị địa chỉ của item
    @Override
    public String toString() { return provinceName; }
}