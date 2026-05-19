package com.example.itstore.api;

import com.example.itstore.model.GhnDistrict;
import com.example.itstore.model.GhnFeeData;
import com.example.itstore.model.GhnFeeRequest;
import com.example.itstore.model.GhnProvince;
import com.example.itstore.model.GhnResponse;
import com.example.itstore.model.GhnWard;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

public class GhnApiClient {
    private static final String BASE_URL = "https://online-gateway.ghn.vn/shiip/public-api/";
    private static Retrofit retrofit = null;


    public static GhnApiService getApiService() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit.create(GhnApiService.class);
    }

    public interface GhnApiService {
        @GET("master-data/province")
        Call<GhnResponse<List<GhnProvince>>> getProvinces(@Header("Token") String token);

        @GET("master-data/district")
        Call<GhnResponse<List<GhnDistrict>>> getDistricts(@Header("Token") String token, @Query("province_id") int provinceId);

        @GET("master-data/ward")
        Call<GhnResponse<List<GhnWard>>> getWards(@Header("Token") String token, @Query("district_id") int districtId);

//        @POST("v2/shipping-order/fee")
//        Call<GhnResponse<GhnFeeData>> getShippingFee(
//                @retrofit2.http.Header("Token") String token,
//                @retrofit2.http.Header("ShopId") int shopId,
//                @retrofit2.http.Body GhnFeeRequest request
//        );
    }
}
