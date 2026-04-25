package com.example.itstore.model;

public class GhnFeeRequest {
    public int service_type_id = 2; // Mã 2: Giao hàng tiêu chuẩn (Thương mại điện tử)
    public int from_district_id;
    public int to_district_id;
    public String to_ward_code;
    public int weight;

    public GhnFeeRequest(int from_district_id, int to_district_id, String to_ward_code, int weight) {
        this.from_district_id = from_district_id;
        this.to_district_id = to_district_id;
        this.to_ward_code = to_ward_code;
        this.weight = weight;
    }
}