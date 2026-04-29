package com.example.itstore.viewmodel;

import static androidx.lifecycle.AndroidViewModel_androidKt.getApplication;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.itstore.api.RetrofitClient;
import com.example.itstore.model.CartItem;
import com.example.itstore.model.Coupon;
import com.example.itstore.model.CouponResponse;
import com.example.itstore.utils.CartManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartViewModel extends AndroidViewModel {
    private MutableLiveData<List<CartItem>> cartItemsLiveData = new MutableLiveData<>(new ArrayList<>());
    private MutableLiveData<Double> totalPriceLiveData = new MutableLiveData<>(0.0);
    private MutableLiveData<Boolean> isAllSelectedLiveData = new MutableLiveData<>(false);

    private final MutableLiveData<List<Coupon>> couponList = new MutableLiveData<>();
    private final MutableLiveData<String> couponError = new MutableLiveData<>();

    public CartViewModel(@NonNull Application application) {
        super(application);
    }

    public MutableLiveData<Boolean> getIsAllSelectedLiveData() {
        return isAllSelectedLiveData;
    }
    public MutableLiveData<List<CartItem>> getCartItems() {
        return cartItemsLiveData;
    }
    public MutableLiveData<Double> getTotalPrice() {
        return totalPriceLiveData;
    }
    public LiveData<List<Coupon>> getCouponList() { return couponList; }
    public LiveData<String> getCouponError() { return couponError; }


    public void calculateTotal() {
        double total = 0;
        List<CartItem> currentList = cartItemsLiveData.getValue();
        if (currentList != null) {
            for (CartItem item : currentList) {
                if (item.isSelected()) {
                    total += item.getPrice() * item.getQuantity();
                }
            }
        }
        totalPriceLiveData.setValue(total);
    }
    public void increaseQuantity(CartItem item, int position) {
        List<CartItem> currentList = cartItemsLiveData.getValue();
        if (currentList != null) {
            item.setQuantity(item.getQuantity() + 1);
            cartItemsLiveData.setValue(currentList);
            calculateTotal();
        }
    }
    public void decreaseQuantity(CartItem item, int position) {
        List<CartItem> currentList = cartItemsLiveData.getValue();
        if (currentList != null) {
            if (item.getQuantity() > 1) {
                item.setQuantity(item.getQuantity() - 1);
            } else {
                currentList.remove(position);
            }
            calculateTotal();
            cartItemsLiveData.setValue(currentList);
        }
    }
    public void removeItem(int position) {
        List<CartItem> currentList = cartItemsLiveData.getValue();
        if (currentList != null && position < currentList.size()) {
            currentList.remove(position);
            cartItemsLiveData.setValue(currentList);
            calculateTotal();
        }
    }
    public void toggleSelectAll(boolean isSelectAll) {
        List<CartItem> currentList = cartItemsLiveData.getValue();
        if (currentList != null) {
            for (CartItem item : currentList) {
                item.setSelected(isSelectAll);
            }
            calculateTotal();
            cartItemsLiveData.setValue(currentList);
            isAllSelectedLiveData.setValue(isSelectAll);
        }
    }
    public void checkAllSelectedStatus() {
        List<CartItem> currentList = cartItemsLiveData.getValue();
        if (currentList == null || currentList.isEmpty()) {
            return;
        }

        boolean allChecked = true;
        for (CartItem item : currentList) {
            if (!item.isSelected()) {
                allChecked = false;
                break;
            }
        }
        isAllSelectedLiveData.setValue(allChecked);
    }
    public void loadCartFromManager() {
        List<CartItem> realCart = CartManager.getInstance().getCartList();
        if (realCart != null) {
            cartItemsLiveData.setValue(realCart);
            calculateTotal();
        }
    }
    public void loadCartFromApi(List<CartItem> itemsFromApi) {
        cartItemsLiveData.setValue(itemsFromApi);
        calculateTotal();
    }

    public void fetchActiveCoupons() {
        RetrofitClient.getApiService(getApplication()).getCoupons().enqueue(new Callback<CouponResponse>() {
            @Override
            public void onResponse(Call<CouponResponse> call, Response<CouponResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    couponList.setValue(response.body().getData());
                } else {
                    couponError.setValue("Không thể tải danh sách khuyến mãi");
                }
            }

            @Override
            public void onFailure(Call<CouponResponse> call, Throwable t) {
                couponError.setValue("Lỗi kết nối server");
            }
        });
    }
}
