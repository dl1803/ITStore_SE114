package com.example.itstore.viewmodel;

import android.app.Application;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.itstore.api.RetrofitClient;
import com.example.itstore.model.AddCartItemRequest;
import com.example.itstore.model.CartItem;
import com.example.itstore.model.CartResponse;
import com.example.itstore.model.Coupon;
import com.example.itstore.model.CouponResponse;
import com.example.itstore.model.UpdateCartItemRequest;
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

    public MutableLiveData<Boolean> getIsAllSelectedLiveData() { return isAllSelectedLiveData; }
    public MutableLiveData<List<CartItem>> getCartItems() { return cartItemsLiveData; }
    public MutableLiveData<Double> getTotalPrice() { return totalPriceLiveData; }
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
        int newQuantity = item.getQuantity() + 1;
        RetrofitClient.getApiService(getApplication())
                .updateCartItem(item.getVariantId(), new UpdateCartItemRequest(newQuantity))
                .enqueue(new Callback<CartResponse>() {
                    @Override
                    public void onResponse(Call<CartResponse> call, Response<CartResponse> response) {
                        if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                            List<CartItem> currentList = cartItemsLiveData.getValue();
                            if (currentList != null) {
                                item.setQuantity(newQuantity);
                                cartItemsLiveData.setValue(currentList);
                                calculateTotal();
                            }
                        } else {
                            Toast.makeText(getApplication(), "Không thể cập nhật số lượng", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<CartResponse> call, Throwable t) {
                        Toast.makeText(getApplication(), "Lỗi mạng", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void decreaseQuantity(CartItem item, int position) {
        if (item.getQuantity() > 1) {
            int newQuantity = item.getQuantity() - 1;
            RetrofitClient.getApiService(getApplication())
                    .updateCartItem(item.getVariantId(), new UpdateCartItemRequest(newQuantity))
                    .enqueue(new Callback<CartResponse>() {
                        @Override
                        public void onResponse(Call<CartResponse> call, Response<CartResponse> response) {
                            if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                                List<CartItem> currentList = cartItemsLiveData.getValue();
                                if (currentList != null) {
                                    item.setQuantity(newQuantity);
                                    cartItemsLiveData.setValue(currentList);
                                    calculateTotal();
                                }
                            } else {
                                Toast.makeText(getApplication(), "Không thể cập nhật số lượng", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<CartResponse> call, Throwable t) {
                            Toast.makeText(getApplication(), "Lỗi mạng", Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            RetrofitClient.getApiService(getApplication())
                    .removeCartItem(item.getVariantId())
                    .enqueue(new Callback<CartResponse>() {
                        @Override
                        public void onResponse(Call<CartResponse> call, Response<CartResponse> response) {
                            if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                                List<CartItem> currentList = cartItemsLiveData.getValue();
                                if (currentList != null) {
                                    currentList.removeIf(cartItem -> cartItem.getVariantId() == item.getVariantId());
                                    cartItemsLiveData.setValue(currentList);
                                    calculateTotal();
                                }
                            } else {
                                Toast.makeText(getApplication(), "Không thể xóa sản phẩm", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<CartResponse> call, Throwable t) {
                            Toast.makeText(getApplication(), "Lỗi mạng", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    public void removeItem(int position) {
        List<CartItem> currentList = cartItemsLiveData.getValue();
        if (currentList == null || position >= currentList.size()) return;
        CartItem item = currentList.get(position);
        RetrofitClient.getApiService(getApplication())
                .removeCartItem(item.getVariantId())
                .enqueue(new Callback<CartResponse>() {
                    @Override
                    public void onResponse(Call<CartResponse> call, Response<CartResponse> response) {
                        if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                            List<CartItem> list = cartItemsLiveData.getValue();
                            if (list != null) {
                                list.removeIf(cartItem -> cartItem.getVariantId() == item.getVariantId());
                                cartItemsLiveData.setValue(list);
                                calculateTotal();
                            }
                        } else {
                            Toast.makeText(getApplication(), "Không thể xóa sản phẩm", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<CartResponse> call, Throwable t) {
                        Toast.makeText(getApplication(), "Lỗi mạng", Toast.LENGTH_SHORT).show();
                    }
                });
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
        if (currentList == null || currentList.isEmpty()) return;
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

    public void fetchCart() {
        RetrofitClient.getApiService(getApplication()).getCart().enqueue(new Callback<CartResponse>() {
            @Override
            public void onResponse(Call<CartResponse> call, Response<CartResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().isSuccess()) {
                        List<CartItem> cartItems = response.body().getData();
                        if (cartItems != null) {
                            cartItemsLiveData.setValue(cartItems);
                            calculateTotal();
                        } else {
                            Toast.makeText(getApplication(), "Lỗi từ server", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        if (response.code() == 401) {
                            Toast.makeText(getApplication(), "Phiên đăng nhập đã hết hạn", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplication(), "Lỗi từ server", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<CartResponse> call, Throwable t) {
                Toast.makeText(getApplication(), "Lỗi mạng: Không thể tải giỏ hàng", Toast.LENGTH_SHORT).show();
            }
        });
    }
}