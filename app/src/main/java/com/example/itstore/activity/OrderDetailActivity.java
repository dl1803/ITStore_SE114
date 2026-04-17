package com.example.itstore.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.itstore.adapter.OrderDetailAdapter;
import com.example.itstore.databinding.ActivityOrderDetailBinding;
import com.example.itstore.model.Order;
import com.example.itstore.model.Product;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class OrderDetailActivity extends AppCompatActivity {

    private ActivityOrderDetailBinding binding;
    private Order currentOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOrderDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        currentOrder = (Order) getIntent().getSerializableExtra("ORDER_DATA");

        if (currentOrder == null) {
            Toast.makeText(this, "Lỗi: Không tải được đơn hàng!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        setupOrderInfo();
        updateBottomButtons();
        setupClickListeners();
    }

    private void setupOrderInfo() {
        binding.btnBack.setOnClickListener(v -> finish());
        DecimalFormat formatter = new DecimalFormat("###,###,###");

        binding.tvOrderId.setText("Mã đơn hàng: #" + currentOrder.getOrderId());
        binding.tvOrderDate.setText("Ngày đặt: 22/02/2026 14:30"); // Sau này thay bằng API thực tế

        String status = currentOrder.getStatus();
        binding.tvOrderStatus.setText("Trạng thái: " + status);
        if (status.equalsIgnoreCase("Đã hủy")) {
            binding.tvOrderStatus.setTextColor(Color.parseColor("#FF3B30"));
        } else if (status.equalsIgnoreCase("Đã giao") || status.equalsIgnoreCase("Hoàn thành")) {
            binding.tvOrderStatus.setTextColor(Color.parseColor("#4CAF50"));
        } else {
            binding.tvOrderStatus.setTextColor(Color.parseColor("#F57C00"));
        }

        // 2. Gắn thông tin Khách hàng (Tạm thời Hardcode vì model Order của sếp chưa có)
        // Khi nào model Order cập nhật, sếp thay thành: currentOrder.getCustomerName()
        binding.tvCustomerName.setText("Nguyễn Đại Vương | 0987654321");
        binding.tvAddress.setText("123 Đường Linh Kiện, Phường 10, Quận 1, TPHCM");

        List<Product> mockProductList = new ArrayList<>();

        // Truyền null cho Variants và Images để fix lỗi đỏ lúc test UI
        Product dummyProduct = new Product(
                1,
                1,
                currentOrder.getProductName(),
                "Mô tả sản phẩm",
                null, // Danh sách Variant (Tạm null)
                null  // Danh sách Image (Tạm null)
        );
        mockProductList.add(dummyProduct);

        OrderDetailAdapter adapter = new OrderDetailAdapter(mockProductList);
        binding.rvProducts.setLayoutManager(new LinearLayoutManager(this));
        binding.rvProducts.setAdapter(adapter);

        // 4. Tính toán tiền bạc
        long subTotal = currentOrder.getTotalPrice(); // Tiền hàng
        long shippingFee = 30000; // Phí ship (Giả lập)
        long totalAmount = subTotal + shippingFee; // Tổng cộng

        binding.tvSubTotal.setText(formatter.format(subTotal) + "đ");
        binding.tvShippingFee.setText(formatter.format(shippingFee) + "đ");
        binding.tvTotal.setText(formatter.format(totalAmount) + "đ");
    }

    // ==========================================
    // HÀM 2: LOGIC ẨN/HIỆN 4 NÚT BOTTOM BAR
    // ==========================================
    private void updateBottomButtons() {
        binding.btnCancelOrderDetail.setVisibility(View.GONE);
        binding.btnRefundOrderDetail.setVisibility(View.GONE);
        binding.btnConfirmReceived.setVisibility(View.GONE);
        binding.btnReviewDetail.setVisibility(View.GONE);

        String status = currentOrder.getStatus();

        switch (status) {
            case "Chờ xác nhận":
                binding.btnCancelOrderDetail.setVisibility(View.VISIBLE);
                break;
            case "Đang giao":
                binding.btnRefundOrderDetail.setVisibility(View.VISIBLE);
                binding.btnConfirmReceived.setVisibility(View.VISIBLE);
                break;
            case "Đã giao":
            case "Hoàn thành":
                binding.btnReviewDetail.setVisibility(View.VISIBLE);
                break;
            case "Đã hủy":
            case "Đang xử lý hoàn tiền":
                break;
        }
    }

    // ==========================================
    // HÀM 3: BẮT SỰ KIỆN CLICK CHO TỪNG NÚT
    // ==========================================
    private void setupClickListeners() {
        binding.btnCancelOrderDetail.setOnClickListener(v -> {
            currentOrder.setStatus("Đã hủy");
            binding.tvOrderStatus.setText("Trạng thái: Đã hủy");
            binding.tvOrderStatus.setTextColor(Color.parseColor("#FF3B30"));
            updateBottomButtons();
            Toast.makeText(this, "Đã hủy đơn hàng thành công!", Toast.LENGTH_SHORT).show();
        });

        binding.btnConfirmReceived.setOnClickListener(v -> {
            currentOrder.setStatus("Hoàn thành");
            binding.tvOrderStatus.setText("Trạng thái: Hoàn thành");
            binding.tvOrderStatus.setTextColor(Color.parseColor("#4CAF50"));
            updateBottomButtons();
            Toast.makeText(this, "Chốt đơn thành công! Hãy đánh giá nhé.", Toast.LENGTH_SHORT).show();
        });

        binding.btnRefundOrderDetail.setOnClickListener(v -> {
            Intent intent = new Intent(OrderDetailActivity.this, RefundRequestActivity.class);
            // intent.putExtra("ORDER_ID", currentOrder.getOrderId());
            startActivity(intent);
        });

        binding.btnReviewDetail.setOnClickListener(v -> {
            Intent intent = new Intent(OrderDetailActivity.this, WriteReviewActivity.class);
            // intent.putExtra("ORDER_ID", currentOrder.getOrderId());
            startActivity(intent);
        });
    }
}