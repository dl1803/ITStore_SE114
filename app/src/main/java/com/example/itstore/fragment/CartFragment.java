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
import com.example.itstore.adapter.CartAdapter;
import com.example.itstore.adapter.DiscountAdapter;
import com.example.itstore.databinding.FragmentCartBinding;
import com.example.itstore.model.CartItem;
import com.example.itstore.model.Discount;
import com.example.itstore.utils.CartManager;
import com.example.itstore.viewmodel.CartViewModel;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;
import java.util.List;
public class CartFragment extends Fragment {
    private FragmentCartBinding binding;
    private CartViewModel cartViewModel;
    private CartAdapter cartAdapter;

    private String selectedVoucherCode = "";
    private double selectedVoucherAmount = 0;

    private Discount appliedDiscount;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentCartBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        cartViewModel = new ViewModelProvider(this).get(CartViewModel.class);
        setupRecyclerView();
        observeViewModel();
        binding.ivBack.setOnClickListener(v -> {
            Navigation.findNavController(v).navigateUp();
        });

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


            startActivity(intent);
        });

        binding.cbBuyAll.setOnClickListener(v -> {
            boolean isChecked = binding.cbBuyAll.isChecked();
            cartViewModel.toggleSelectAll(isChecked);
        });

        cartViewModel.loadCartFromManager();
        List<CartItem> checkCart = CartManager.getInstance().getCartList();
        if (checkCart != null) {
            Toast.makeText(requireContext(), "Trong giỏ đang có: " + checkCart.size() + " món", Toast.LENGTH_SHORT).show();
        }
    }

    private void setupRecyclerView() {
        List<CartItem> currentList = CartManager.getInstance().getCartList();
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
                cartViewModel.getCartItems().setValue(currentList);
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
    }

    // Hàm kiểm tra điều kiện tổng tiền tối thiểu để áp dụng mã giảm giá
    private void validateVoucherWithTotal(double rawTotal) {
        if (appliedDiscount != null) {

            if (rawTotal <= 0 || rawTotal < appliedDiscount.getMinOrderValue()) {

                selectedVoucherCode = "";
                selectedVoucherAmount = 0;
                appliedDiscount = null;

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

        // Mock Data
        List<Discount> myVouchers = new ArrayList<>();
        myVouchers.add(new Discount("UIT_20", "Giảm 20.000đ", "Đơn tối thiểu 150k", "HSD: 31/12/2026", 20000, 150000));
        myVouchers.add(new Discount("UIT_50", "Giảm 50.000đ", "Đơn tối thiểu 500k", "HSD: 31/12/2026", 50000, 500000));

        rvDiscounts.setLayoutManager(new LinearLayoutManager(requireContext()));

        DiscountAdapter discountAdapter = new DiscountAdapter(myVouchers, discount -> {
            double currentTotal = cartViewModel.getTotalPrice().getValue() != null ? cartViewModel.getTotalPrice().getValue() : 0;

            if (currentTotal <= 0) {
                Toast.makeText(requireContext(), "Vui lòng chọn sản phẩm trước khi áp mã!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (currentTotal < discount.getMinOrderValue()) {
                String requiredAmount = String.format("%,.0f đ", discount.getMinOrderValue());
                Toast.makeText(requireContext(), "Đơn hàng chưa đạt tối thiểu " + requiredAmount + "!", Toast.LENGTH_LONG).show();
                return;
            }

            selectedVoucherCode = discount.getCode();
            selectedVoucherAmount = discount.getAmount();

            this.appliedDiscount = discount;

            binding.tvCartVoucherCode.setText(selectedVoucherCode);
            binding.tvCartVoucherCode.setTextColor(getResources().getColor(R.color.orange_primary));

            updateTotalPriceUI(currentTotal);

            Toast.makeText(requireContext(), "Đã lưu mã: " + selectedVoucherCode, Toast.LENGTH_SHORT).show();
            bottomSheetDialog.dismiss();
        });

        rvDiscounts.setAdapter(discountAdapter);

        if (btnClose != null) btnClose.setOnClickListener(v -> bottomSheetDialog.dismiss());

        bottomSheetDialog.show();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}