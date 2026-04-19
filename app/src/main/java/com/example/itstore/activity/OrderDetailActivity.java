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
import com.example.itstore.model.OrderItem;
import com.example.itstore.model.OrderTimeline;
import com.example.itstore.model.Product;
import com.example.itstore.model.ProductImage;
import com.example.itstore.model.ProductVariant;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.example.itstore.adapter.OrderTimelineAdapter;
import com.example.itstore.databinding.DialogTimelineBinding;

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
        binding.tvOrderDate.setText("Ngày đặt: 22/02/2026 14:30");

        String status = currentOrder.getStatus();
        binding.tvOrderStatus.setText("Trạng thái: " + status);
        if (status.equalsIgnoreCase("Đã hủy")) {
            binding.tvOrderStatus.setTextColor(Color.parseColor("#FF3B30"));
        } else if (status.equalsIgnoreCase("Đã giao") || status.equalsIgnoreCase("Hoàn thành")) {
            binding.tvOrderStatus.setTextColor(Color.parseColor("#4CAF50"));
        } else {
            binding.tvOrderStatus.setTextColor(Color.parseColor("#F57C00"));
        }

        binding.tvCustomerName.setText("Nguyễn Đại Vương | 0987654321");
        binding.tvAddress.setText("123 Đường Linh Kiện, Phường 10, Quận 1, TPHCM");

        List<OrderItem> mockOrderItemList = new ArrayList<>();

        List<ProductVariant> variants = new ArrayList<>();
        variants.add(new ProductVariant(1, currentOrder.getProductType(), (double) currentOrder.getTotalPrice(), 0, 100));

        List<ProductImage> listImages = new ArrayList<>();
        listImages.add(new ProductImage(1, String.valueOf(currentOrder.getImageRes()), true));

        Product dummyProduct = new Product(
                1,
                1,
                currentOrder.getProductName(),
                currentOrder.getProductType(),
                variants,
                listImages
        );
        OrderItem dummyItem = new OrderItem(
                dummyProduct,
                currentOrder.getQuantity(),
                currentOrder.getTotalPrice()
        );

        mockOrderItemList.add(dummyItem);

        OrderDetailAdapter adapter = new OrderDetailAdapter(mockOrderItemList);
        binding.rvProducts.setLayoutManager(new LinearLayoutManager(this));
        binding.rvProducts.setAdapter(adapter);

        // Tính tiền tổng
        long subTotal = currentOrder.getTotalPrice();
        long shippingFee = 30000;
        long totalAmount = subTotal + shippingFee;

        binding.tvSubTotal.setText(formatter.format(subTotal) + "đ");
        binding.tvShippingFee.setText(formatter.format(shippingFee) + "đ");
        binding.tvTotal.setText(formatter.format(totalAmount) + "đ");
    }

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
                binding.btnConfirmReceived.setVisibility(View.VISIBLE);
                break;
            case "Đã giao":
                binding.btnRefundOrderDetail.setVisibility(View.VISIBLE);
                binding.btnReviewDetail.setVisibility(View.VISIBLE);
                break;
            case "Đã hủy":
            case "Đang xử lý hoàn tiền":
                break;
        }
    }

    private void setupClickListeners() {
        binding.btnCancelOrderDetail.setOnClickListener(v -> {
            currentOrder.setStatus("Đã hủy");
            binding.tvOrderStatus.setText("Trạng thái: Đã hủy");
            binding.tvOrderStatus.setTextColor(Color.parseColor("#FF3B30"));
            updateBottomButtons();

            Intent resultIntent = new Intent();
            resultIntent.putExtra("UPDATED_ORDER", currentOrder);
            setResult(RESULT_OK, resultIntent);

            Toast.makeText(this, "Đã hủy đơn hàng thành công!", Toast.LENGTH_SHORT).show();
        });
        binding.tvViewTimeline.setOnClickListener(v -> showTimelineDialog());
        binding.btnConfirmReceived.setOnClickListener(v -> {
            currentOrder.setStatus("Đã giao");
            binding.tvOrderStatus.setText("Trạng thái: Đã giao");
            binding.tvOrderStatus.setTextColor(Color.parseColor("#4CAF50"));
            updateBottomButtons();

            Intent resultIntent = new Intent();
            resultIntent.putExtra("UPDATED_ORDER", currentOrder);
            setResult(RESULT_OK, resultIntent);

            Toast.makeText(this, "Chốt đơn thành công! Hãy đánh giá nhé.", Toast.LENGTH_SHORT).show();
        });

        binding.btnRefundOrderDetail.setOnClickListener(v -> {
            Intent intent = new Intent(OrderDetailActivity.this, RefundRequestActivity.class);
            intent.putExtra("ORDER_DATA", currentOrder);
            startActivity(intent);
        });

        binding.btnReviewDetail.setOnClickListener(v -> {
            Intent intent = new Intent(OrderDetailActivity.this, WriteReviewActivity.class);
            startActivity(intent);
        });
    }
    private void showTimelineDialog() {
        BottomSheetDialog dialog = new BottomSheetDialog(this);
        com.example.itstore.databinding.DialogTimelineBinding dialogBinding =
                com.example.itstore.databinding.DialogTimelineBinding.inflate(getLayoutInflater());
        dialog.setContentView(dialogBinding.getRoot());

        List<OrderTimeline> realTimelines = new ArrayList<>();
        String orderTime = currentOrder.getOrderDate();
        String currentStatus = currentOrder.getStatus();
        realTimelines.add(new OrderTimeline("Đặt hàng thành công", "Hệ thống đã ghi nhận đơn hàng của bạn.\n" + orderTime));

        if (currentStatus.equalsIgnoreCase("Đã hủy")) {
            realTimelines.add(0, new OrderTimeline("Đơn hàng đã bị hủy", "Đơn hàng đã bị hủy theo yêu cầu."));
        } else {
            if (!currentStatus.equalsIgnoreCase("Chờ xác nhận")) {
                realTimelines.add(0, new OrderTimeline("Đã xác nhận", "Người bán đã xác nhận đơn hàng và đang chuẩn bị."));
            }

            if (currentStatus.equalsIgnoreCase("Đang chuẩn bị hàng") ||
                    currentStatus.equalsIgnoreCase("Đã đóng gói") ||
                    currentStatus.equalsIgnoreCase("Đang giao") ||
                    currentStatus.equalsIgnoreCase("Đã giao")) {
                realTimelines.add(0, new OrderTimeline("Đang chuẩn bị hàng", "Kho đang xuất linh kiện và tiến hành đóng gói."));
            }

            if (currentStatus.equalsIgnoreCase("Đã đóng gói") ||
                    currentStatus.equalsIgnoreCase("Đang giao") ||
                    currentStatus.equalsIgnoreCase("Đã giao")) {
                realTimelines.add(0, new OrderTimeline("Đã đóng gói", "Đơn hàng đã được bàn giao cho đơn vị vận chuyển (Giao Hàng Tiết Kiệm)."));
            }

            if (currentStatus.equalsIgnoreCase("Đang giao") ||
                    currentStatus.equalsIgnoreCase("Đã giao")) {
                realTimelines.add(0, new OrderTimeline("Đang giao hàng", "Shipper đang trên đường giao hàng đến bạn. Vui lòng chú ý điện thoại."));
            }

            if (currentStatus.equalsIgnoreCase("Đã giao")) {
                realTimelines.add(0, new OrderTimeline("Giao hàng thành công", "Kiện hàng đã được giao thành công đến người nhận."));
            }
        }

        OrderTimelineAdapter adapter = new OrderTimelineAdapter(realTimelines);
        dialogBinding.rvOrderTimeline.setLayoutManager(new LinearLayoutManager(this));
        dialogBinding.rvOrderTimeline.setAdapter(adapter);
        dialog.show();
    }
}