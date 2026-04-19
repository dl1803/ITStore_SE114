package com.example.itstore.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.itstore.R;
import com.example.itstore.adapter.CheckoutAdapter;
import com.example.itstore.adapter.DiscountAdapter;
import com.example.itstore.databinding.ActivityCheckoutBinding;
import com.example.itstore.model.CartItem;
import com.example.itstore.model.Discount;
import com.example.itstore.model.Order;
import com.example.itstore.utils.CartManager;
import com.example.itstore.viewmodel.CheckoutViewModel;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class CheckoutActivity extends AppCompatActivity {

    ActivityCheckoutBinding binding;
    private CheckoutViewModel checkoutViewModel;
    private CheckoutAdapter adapter;

    private String appliedVoucherCode = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        
        super.onCreate(savedInstanceState);
        binding = ActivityCheckoutBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());

        binding.btnBack.setOnClickListener(v -> finish());

        checkoutViewModel = new ViewModelProvider(this).get(CheckoutViewModel.class);
        binding.rvCheckoutItems.setLayoutManager(new LinearLayoutManager(this));

        observeViewModel();

        binding.cardInfo.setOnClickListener(v -> {
            showAddressSelectionDialog();
        });

        binding.layoutDiscount.setOnClickListener(v -> {
            showDiscountBottomSheet();
        });
        
        List<CartItem> receivedItems = CartManager.getInstance().getCheckoutList();

        if (receivedItems != null && !receivedItems.isEmpty()) {
            checkoutViewModel.loadCheckoutData(receivedItems);
            Intent intent = getIntent();

            String passedCode = intent.getStringExtra("VOUCHER_CODE");
            double passedAmount = intent.getDoubleExtra("VOUCHER_AMOUNT", 0.0);

            if (passedCode != null && passedAmount > 0) {
                appliedVoucherCode = passedCode;
                checkoutViewModel.applyDiscount(passedAmount);
            }
        } else {
            Toast.makeText(this, "Lỗi: Không tìm thấy sản phẩm nào!", Toast.LENGTH_SHORT).show();
            finish();
        }

        binding.rbCod.setChecked(true);

        binding.btnCheckout.setOnClickListener(v -> {
            String paymentMethod = "COD";
            if (binding.rbMomo.isChecked()) paymentMethod = "MOMO";
            if (binding.rbBankTransfer.isChecked()) paymentMethod = "BANK";

            List<CartItem> purchasedItems = checkoutViewModel.getCheckoutItems().getValue();
            if (purchasedItems == null || purchasedItems.isEmpty()) {
                Toast.makeText(this, "Đơn hàng rỗng!", Toast.LENGTH_SHORT).show();
                return;
            }

            int randomNum = new java.util.Random().nextInt(900000) + 100000;
            String orderId = "ORD-" + randomNum;
            String status = "Chờ xác nhận";

            CartItem firstItem = purchasedItems.get(0);
            String productName = firstItem.getProduct().getName();
            String productType = firstItem.getVariantName();
            int quantity = firstItem.getQuantity();

            int extraItemsCount = 0;
            if (purchasedItems.size() > 1) {
                extraItemsCount = purchasedItems.size() - 1;
            }

            long totalPrice = 0;
            if (checkoutViewModel.getFinalTotalPrice().getValue() != null) {
                double tienDouble = checkoutViewModel.getFinalTotalPrice().getValue();
                totalPrice = (long) tienDouble;
            }

            Order newOrder = new Order(
                    orderId, status, productName, productType, quantity, extraItemsCount, totalPrice, R.drawable.ram1, "22/02/2026 14:30"
            );

            System.out.println("Đã tạo đơn hàng: " + newOrder.getOrderId());

            CartManager.getInstance().clearPurchasedItems();

            Toast.makeText(this, "Đặt hàng thành công! Mã: " + newOrder.getOrderId(), Toast.LENGTH_LONG).show();


            Intent intent = new Intent(this, OrderSuccessActivity.class);

            intent.putExtra("ORDER_DATA", newOrder);
            startActivity(intent);
            finish();
        });
    }

    // TÍNH NĂNG 1: CHỌN ĐỊA CHỈ TỪ DANH SÁCH
    private void showAddressSelectionDialog() {
        String[] addresses = {
                "UIT Thủ Đức, TP.HCM",
                "Quận 1, TP.HCM (Nhà riêng)",
                "Quận 7, TP.HCM (Công ty)"
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Chọn địa chỉ nhận hàng");

        builder.setItems(addresses, (dialog, which) -> {
            String selectedAddress = addresses[which];

            binding.tvAddress.setText(selectedAddress);

            Toast.makeText(this, "Đã chọn: " + selectedAddress, Toast.LENGTH_SHORT).show();
        });
        builder.show();
    }

    // TÍNH NĂNG 2: MỞ BOTTOM SHEET CHỌN VOUCHER
    private void showDiscountBottomSheet() {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);

        View dialogView = getLayoutInflater().inflate(R.layout.dialog_discount, null);
        bottomSheetDialog.setContentView(dialogView);

        EditText edtCode = dialogView.findViewById(R.id.edtVoucherCode);
        Button btnApply = dialogView.findViewById(R.id.btnApplyVoucher);
        RecyclerView rvDiscounts = dialogView.findViewById(R.id.rvDiscountList);
        ImageView btnClose = dialogView.findViewById(R.id.btnCloseDialog);

        List<Discount> myVouchers = new ArrayList<>();
        myVouchers.add(new Discount("UIT_20", "Giảm 20.000đ", "Đơn tối thiểu 150k", "HSD: 31/12/2026", 20000, 150000));
        myVouchers.add(new Discount("UIT_50", "Giảm 50.000đ", "Đơn tối thiểu 500k", "HSD: 31/12/2026", 50000, 500000));

        rvDiscounts.setLayoutManager(new LinearLayoutManager(this));
        DiscountAdapter discountAdapter = new DiscountAdapter(myVouchers, discount -> {
            applyDiscountToCart(discount.getCode(), discount.getAmount(), discount.getMinOrderValue());
            bottomSheetDialog.dismiss();
        });
        rvDiscounts.setAdapter(discountAdapter);

        btnApply.setOnClickListener(v -> {
            String inputCode = edtCode.getText().toString().trim();
            if (inputCode.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập mã!", Toast.LENGTH_SHORT).show();
                return;
            }
            Discount foundDiscount = null;
            for (Discount discount : myVouchers) {
                if (discount.getCode().equalsIgnoreCase(inputCode)) {
                    foundDiscount = discount;
                    break;
                }
            }

            if (foundDiscount != null) {
                applyDiscountToCart(foundDiscount.getCode(), foundDiscount.getAmount(), foundDiscount.getMinOrderValue());
                bottomSheetDialog.dismiss();
            } else {
                Toast.makeText(this, "Mã giảm giá không hợp lệ!", Toast.LENGTH_SHORT).show();
            }
        });

        if (btnClose != null) {
            btnClose.setOnClickListener(v -> bottomSheetDialog.dismiss());
        }

        bottomSheetDialog.show();


    }

    private void applyDiscountToCart(String code, double amount, double minOrderValue) {
        double currentSubtotal = 0;
        if (checkoutViewModel.getSubtotalPrice().getValue() != null) {
            currentSubtotal = checkoutViewModel.getSubtotalPrice().getValue();
        }

        if (currentSubtotal <= 0) {
            Toast.makeText(this, "Đơn hàng trống!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (currentSubtotal < minOrderValue) {
            String requiredAmount = String.format("%,.0f đ", minOrderValue);
            Toast.makeText(this, "Chưa đạt mức tối thiểu " + requiredAmount + " để dùng mã này!", Toast.LENGTH_LONG).show();
            return;
        }

        appliedVoucherCode = code;
        Toast.makeText(this, "Đã áp dụng mã: " + code, Toast.LENGTH_SHORT).show();

        checkoutViewModel.applyDiscount(amount);
    }



    private void observeViewModel() {
        checkoutViewModel.getCheckoutItems().observe(this, items -> {
            adapter = new CheckoutAdapter(items);
            binding.rvCheckoutItems.setAdapter(adapter);
            binding.rvCheckoutItems.setNestedScrollingEnabled(false);
        });

        checkoutViewModel.getSubtotalPrice().observe(this, price ->
                binding.tvSubtotalPrice.setText(String.format("%,.0f đ", price)));

        checkoutViewModel.getShippingFee().observe(this, fee ->
                binding.tvShippingPrice.setText(String.format("%,.0f đ", fee)));

        checkoutViewModel.getTotalDiscount().observe(this, discount -> {
            if (discount > 0) {
                binding.tvDiscount.setText(String.format("%,.0f đ", discount));

                binding.tvVoucherDiscount.setText(String.format("-%,.0f đ", discount));

            } else {
                binding.tvDiscount.setText("0đ");
                binding.tvVoucherDiscount.setText("0đ");
            }
        });

        checkoutViewModel.getFinalTotalPrice().observe(this, finalTotal -> {
            String formattedTotal = String.format("%,.0f đ", finalTotal);
            binding.tvTotalPrice.setText(formattedTotal);
            binding.tvTotal.setText(formattedTotal);
        });

    }
}