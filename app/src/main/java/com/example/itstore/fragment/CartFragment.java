package com.example.itstore.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.itstore.R;
import com.example.itstore.activity.CheckoutActivity;
import com.example.itstore.activity.LoginActivity;
import com.example.itstore.activity.ProductDetailActivity;
import com.example.itstore.adapter.CartAdapter;
import com.example.itstore.adapter.DiscountAdapter;
import com.example.itstore.api.RetrofitClient;
import com.example.itstore.databinding.FragmentCartBinding;
import com.example.itstore.model.CartItem;
import com.example.itstore.model.Coupon;
import com.example.itstore.model.CouponResponse;
import com.example.itstore.model.Discount;
import com.example.itstore.model.Product;
import com.example.itstore.utils.CartManager;
import com.example.itstore.utils.SharedPrefsManager;
import com.example.itstore.viewmodel.CartViewModel;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartFragment extends Fragment {
    private FragmentCartBinding binding;
    private CartViewModel cartViewModel;
    private CartAdapter cartAdapter;

    private String selectedVoucherCode = "";
    private double selectedVoucherAmount = 0;
    private int selectedVoucherId = -1;

    private Coupon appliedCoupon;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentCartBinding.inflate(inflater, container, false);
        String token = SharedPrefsManager.getInstance(requireContext()).getAccessToken();
        if (token == null || token.isEmpty()) {
            binding.rvCart.setVisibility(View.GONE);
            binding.layoutCartDiscount.setVisibility(View.GONE);
            binding.layoutCheckoutTotal.setVisibility(View.GONE);
            binding.layoutRequireLogin.setVisibility(View.VISIBLE);
            binding.btnLoginNow.setOnClickListener(v -> {
                startActivity(new Intent(requireContext(), LoginActivity.class));
            });
        } else {
            binding.layoutRequireLogin.setVisibility(View.GONE);
            binding.rvCart.setVisibility(View.VISIBLE);
            binding.layoutCartDiscount.setVisibility(View.VISIBLE);
            binding.layoutCheckoutTotal.setVisibility(View.VISIBLE);
        }
        return binding.getRoot();
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        cartViewModel = new ViewModelProvider(this).get(CartViewModel.class);
        setupRecyclerView();
        observeViewModel();

        binding.layoutCartDiscount.setOnClickListener(v -> {
            showDiscountBottomSheet();
        });

        binding.btnCheckout.setOnClickListener(v -> {
            List<CartItem> itemsToBuy = new ArrayList<>();
            List<CartItem> currentCart = cartViewModel.getCartItems().getValue();

            if (currentCart != null) {
                for (CartItem item : currentCart) {
                    if (item.isSelected()) {
                        itemsToBuy.add(item);
                    }
                }
            }
            if (itemsToBuy.isEmpty()) {
                Toast.makeText(requireContext(), "Bạn chưa chọn sản phẩm nào để mua!", Toast.LENGTH_SHORT).show();
                return;
            }
            CartManager.getInstance().setCheckoutList(itemsToBuy);
            Intent intent = new Intent(requireActivity(), CheckoutActivity.class);

            intent.putExtra("VOUCHER_CODE", selectedVoucherCode);
            intent.putExtra("VOUCHER_AMOUNT", selectedVoucherAmount);
            intent.putExtra("VOUCHER_ID", selectedVoucherId);


            startActivity(intent);
        });

        binding.cbBuyAll.setOnClickListener(v -> {
            boolean isChecked = binding.cbBuyAll.isChecked();
            cartViewModel.toggleSelectAll(isChecked);
        });

        cartViewModel.fetchCart();

        cartViewModel.fetchActiveCoupons();

    }

    private void setupRecyclerView() {
        cartAdapter = new CartAdapter(new ArrayList<>(), new CartAdapter.CartClickListener() {
            @Override
            public void onIncrease(CartItem item, int position) {
                cartViewModel.increaseQuantity(item, position);
            }

            @Override
            public void onDecrease(CartItem item, int position) {
                cartViewModel.decreaseQuantity(item, position);
            }

            @Override
            public void onDelete(CartItem item, int position) {
                cartViewModel.removeItem(position);
                Toast.makeText(requireContext(), "Đã xóa sản phẩm", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onItemSelected(CartItem item, boolean isChecked) {
                item.setSelected(isChecked);
                cartViewModel.checkAllSelectedStatus();
                cartViewModel.calculateTotal();
                cartViewModel.getCartItems().setValue(cartViewModel.getCartItems().getValue());
            }
            @Override
            public void onProductClick(Product product) {
                Intent intent = new Intent(requireContext(), ProductDetailActivity.class);
                intent.putExtra("PRODUCT_INFO", product);
                startActivity(intent);
            }
        });
        binding.rvCart.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rvCart.setAdapter(cartAdapter);
    }

    private void observeViewModel() {
        cartViewModel.getCartItems().observe(getViewLifecycleOwner(), items -> {
            if (items != null) {
                cartAdapter.setCartList(items);
            }
            int selectedCount = 0;
            for (CartItem item : items) {
                if (item.isSelected()) selectedCount++;
            }
            binding.btnCheckout.setText("Mua hàng (" + selectedCount + ")");
        });

        cartViewModel.getTotalPrice().observe(getViewLifecycleOwner(), rawTotal -> {
            validateVoucherWithTotal(rawTotal);

            updateTotalPriceUI(rawTotal);
        });

        cartViewModel.getIsAllSelectedLiveData().observe(getViewLifecycleOwner(), isAllSelected -> {
            binding.cbBuyAll.setChecked(isAllSelected);
        });

        cartViewModel.getCouponError().observe(getViewLifecycleOwner(), error -> {
            if (error != null) Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show();
        });
    }

    // Hàm kiểm tra điều kiện tổng tiền tối thiểu để áp dụng mã giảm giá
    private void validateVoucherWithTotal(double rawTotal) {
        if (appliedCoupon != null) {

            if (rawTotal <= 0 || rawTotal < appliedCoupon.getMinOrderValue()) {

                selectedVoucherCode = "";
                selectedVoucherAmount = 0;
                selectedVoucherId = -1;
                appliedCoupon = null;

                binding.tvCartVoucherCode.setText("Chọn hoặc nhập mã");
                binding.tvCartVoucherCode.setTextColor(Color.parseColor("#888888"));

                Toast.makeText(requireContext(), "Đã gỡ mã do không đủ điều kiện tối thiểu!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void updateTotalPriceUI(double rawTotal) {
        if (rawTotal == 0) {
            binding.tvTotalPrice.setText("0 đ");
            binding.tvCartDiscount.setVisibility(View.GONE);
            return;
        }
        if (appliedCoupon != null) {
            if ("percent".equals(appliedCoupon.getDiscountType())) {
                selectedVoucherAmount = rawTotal * (appliedCoupon.getDiscountValue() / 100.0);
            } else {
                selectedVoucherAmount = appliedCoupon.getDiscountValue();
            }
        }
        double finalTotal = rawTotal - selectedVoucherAmount;
        if (finalTotal < 0) finalTotal = 0;

        binding.tvTotalPrice.setText(String.format("%,.0f đ", finalTotal));

        if (selectedVoucherAmount > 0) {
            binding.tvCartDiscount.setVisibility(View.VISIBLE);
            binding.tvCartDiscount.setText(String.format("Tiết kiệm: -%,.0f đ", selectedVoucherAmount));
        } else {
            binding.tvCartDiscount.setVisibility(View.GONE);
        }
    }

    private void showDiscountBottomSheet() {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(requireContext());
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_discount, null);
        bottomSheetDialog.setContentView(dialogView);

        EditText edtCode = dialogView.findViewById(R.id.edtVoucherCode);
        Button btnApply = dialogView.findViewById(R.id.btnApplyVoucher);
        RecyclerView rvDiscounts = dialogView.findViewById(R.id.rvDiscountList);
        ImageView btnClose = dialogView.findViewById(R.id.btnCloseDialog);

        List<Coupon> coupons = cartViewModel.getCouponList().getValue();

        if (coupons == null || coupons.isEmpty()) {
            Toast.makeText(requireContext(), "Hiện tại chưa có mã giảm giá nào!", Toast.LENGTH_SHORT).show();
            cartViewModel.fetchActiveCoupons();
            return;
        }

        rvDiscounts.setLayoutManager(new LinearLayoutManager(requireContext()));
        DiscountAdapter discountAdapter = new DiscountAdapter(coupons, coupon -> {
            applyDiscountToCart(coupon);
            bottomSheetDialog.dismiss();
        });
        rvDiscounts.setAdapter(discountAdapter);

        btnApply.setOnClickListener(v -> {
            String inputCode = edtCode.getText().toString().trim();

            if (inputCode.isEmpty()) {
                Toast.makeText(requireContext(), "Vui lòng nhập mã!", Toast.LENGTH_SHORT).show();
                return;
            }
            Coupon foundCoupon = null;
            for (Coupon c : coupons) {
                if (c.getCode().equalsIgnoreCase(inputCode)) {
                    foundCoupon = c;
                    break;
                }
            }

            if (foundCoupon != null) {
                applyDiscountToCart(foundCoupon);
                bottomSheetDialog.dismiss();
            } else {
                Toast.makeText(requireContext(), "Mã giảm giá không hợp lệ!", Toast.LENGTH_SHORT).show();
            }
        });

        if (btnClose != null) {
            btnClose.setOnClickListener(v -> bottomSheetDialog.dismiss());
        }

        bottomSheetDialog.show();
    }

    private void applyDiscountToCart(Coupon coupon) {
        double currentTotal = cartViewModel.getTotalPrice().getValue() != null
                ? cartViewModel.getTotalPrice().getValue() : 0;

        if (currentTotal <= 0) {
            Toast.makeText(requireContext(), "Vui lòng chọn sản phẩm trước khi áp mã!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (currentTotal < coupon.getMinOrderValue()) {
            String requiredAmount = String.format("%,.0f đ", coupon.getMinOrderValue());
            Toast.makeText(requireContext(), "Đơn hàng chưa đạt tối thiểu " + requiredAmount + "!", Toast.LENGTH_LONG).show();
            return;
        }

        selectedVoucherCode = coupon.getCode();
        selectedVoucherId = coupon.getId();

        if ("percent".equals(coupon.getDiscountType())) {
            selectedVoucherAmount = currentTotal * (coupon.getDiscountValue() / 100.0);
        } else {
            selectedVoucherAmount = coupon.getDiscountValue();
        }

        appliedCoupon = coupon;

        binding.tvCartVoucherCode.setText(selectedVoucherCode);
        binding.tvCartVoucherCode.setTextColor(getResources().getColor(R.color.orange_primary));

        updateTotalPriceUI(currentTotal);

        Toast.makeText(requireContext(), "Đã lưu mã: " + selectedVoucherCode, Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}