package com.example.itstore.activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.itstore.R;
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

    private String getStatusVN(String rawStatus) {
        if (rawStatus == null) return "Chờ xác nhận";
        switch (rawStatus.toLowerCase()) {
            case "pending": return "Chờ xác nhận";
            case "processing":
            case "delivering": return "Đang giao";
            case "delivered":
            case "completed": return "Đã giao";
            case "cancelled": return "Đã hủy";
            default: return "Chờ xác nhận";
        }
    }

    private int getStatusColor(String statusVN) {
        switch (statusVN) {
            case "Đã hủy": return Color.parseColor("#FF3B30"); // Đỏ
            case "Đã giao": return Color.parseColor("#4CAF50"); // Xanh lá
            case "Đang giao": return Color.parseColor("#2196F3"); // Xanh dương
            default: return Color.parseColor("#F57C00"); // Cam
        }
    }

    private void setupOrderInfo() {
        binding.btnBack.setOnClickListener(v -> finish());
        DecimalFormat formatter = new DecimalFormat("###,###,###");

        binding.tvOrderId.setText("Mã đơn hàng: #" + currentOrder.getOrderId());
        binding.tvOrderDate.setText("Ngày đặt: " + currentOrder.getOrderDate());

        String statusVN = getStatusVN(currentOrder.getStatus());
        binding.tvOrderStatus.setText("Trạng thái: " + statusVN);
        binding.tvOrderStatus.setTextColor(getStatusColor(statusVN));

        List<OrderItem> itemList = currentOrder.getItems();

        if (itemList == null) {
            itemList = new ArrayList<>();
        }

        OrderDetailAdapter adapter = new OrderDetailAdapter(itemList);
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

        String statusVN = getStatusVN(currentOrder.getStatus());

        switch (statusVN) {
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
                break;
        }
    }

    private void setupClickListeners() {
        binding.btnCancelOrderDetail.setOnClickListener(v -> {
            showCancelReasonDialog();
        });
        binding.tvViewTimeline.setOnClickListener(v -> showTimelineDialog());
        binding.btnConfirmReceived.setOnClickListener(v -> {
            currentOrder.setStatus("completed");
            String statusVN = getStatusVN(currentOrder.getStatus());
            binding.tvOrderStatus.setText("Trạng thái: " + statusVN);
            binding.tvOrderStatus.setTextColor(getStatusColor(statusVN));
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


    private void showCancelReasonDialog(){
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_cancel_order);

        Window window = dialog.getWindow();
        if (window != null) {
            window.setLayout(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
        }

        RadioGroup rgReasons = dialog.findViewById(R.id.rgCancelReasons);
        EditText edtOtherReason = dialog.findViewById(R.id.edtOtherReason);
        AppCompatButton btnConfirm = dialog.findViewById(R.id.btnConfirm);
        AppCompatButton btnClose = dialog.findViewById(R.id.btnClose);

        rgReasons.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.rbReasonOther) {
                edtOtherReason.setVisibility(View.VISIBLE);
            } else {
                edtOtherReason.setVisibility(View.GONE);
            }
        });

        btnClose.setOnClickListener(v -> dialog.dismiss());

        btnConfirm.setOnClickListener(v -> {
            int selectedId = rgReasons.getCheckedRadioButtonId();
            if (selectedId ==-1) {
                Toast.makeText(this, "Vui lòng chọn lý do hủy đơn hàng!", Toast.LENGTH_SHORT).show();
                return;
            }

            currentOrder.setStatus("cancelled");
            String statusVN = getStatusVN(currentOrder.getStatus());
            binding.tvOrderStatus.setText("Trạng thái: " + statusVN);
            binding.tvOrderStatus.setTextColor(getStatusColor(statusVN));
            updateBottomButtons();

            Intent resultIntent = new Intent();
            resultIntent.putExtra("UPDATED_ORDER", currentOrder);
            setResult(RESULT_OK, resultIntent);

            Toast.makeText(this, "Đã hủy đơn hàng thành công!", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        });
        dialog.show();
      }
    private void showTimelineDialog() {
        BottomSheetDialog dialog = new BottomSheetDialog(this);
        com.example.itstore.databinding.DialogTimelineBinding dialogBinding =
                com.example.itstore.databinding.DialogTimelineBinding.inflate(getLayoutInflater());
        dialog.setContentView(dialogBinding.getRoot());

        List<OrderTimeline> realTimelines = new ArrayList<>();
        String orderTime = currentOrder.getOrderDate();
        String currentStatus = currentOrder.getStatus();

        String statusVN = getStatusVN(currentOrder.getStatus());

        realTimelines.add(new OrderTimeline("Đặt hàng thành công", "Hệ thống đã ghi nhận đơn hàng của bạn.\n" + orderTime));

        if (statusVN.equals("Đã hủy")) {
            realTimelines.add(0, new OrderTimeline("Đơn hàng đã bị hủy", "Đơn hàng đã bị hủy theo yêu cầu."));
        } else {
            if (!statusVN.equals("Chờ xác nhận")) {
                realTimelines.add(0, new OrderTimeline("Đã xác nhận", "Người bán đã xác nhận đơn hàng và đang chuẩn bị."));
            }

            if (statusVN.equals("Đang giao") || statusVN.equals("Đã giao")) {
                realTimelines.add(0, new OrderTimeline("Đang chuẩn bị hàng", "Kho đang xuất linh kiện và tiến hành đóng gói."));
                realTimelines.add(0, new OrderTimeline("Đã đóng gói", "Đơn hàng đã được bàn giao cho đơn vị vận chuyển (Giao Hàng Tiết Kiệm)."));
                realTimelines.add(0, new OrderTimeline("Đang giao hàng", "Shipper đang trên đường giao hàng đến bạn. Vui lòng chú ý điện thoại."));
            }

            if (statusVN.equals("Đã giao")) {
                realTimelines.add(0, new OrderTimeline("Giao hàng thành công", "Kiện hàng đã được giao thành công đến người nhận."));
            }
        }

        OrderTimelineAdapter adapter = new OrderTimelineAdapter(realTimelines);
        dialogBinding.rvOrderTimeline.setLayoutManager(new LinearLayoutManager(this));
        dialogBinding.rvOrderTimeline.setAdapter(adapter);
        dialog.show();
    }
}