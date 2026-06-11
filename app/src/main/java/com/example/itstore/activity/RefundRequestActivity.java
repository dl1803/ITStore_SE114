package com.example.itstore.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.itstore.R;
import com.example.itstore.adapter.RefundImageAdapter;
import com.example.itstore.adapter.RefundItemAdapter;
import com.example.itstore.databinding.ActivityRefundRequestBinding;
import com.example.itstore.model.Order;
import com.example.itstore.model.OrderItem;
import com.example.itstore.viewmodel.RefundRequestViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RefundRequestActivity extends AppCompatActivity {

    private ActivityRefundRequestBinding binding;
    private RefundRequestViewModel viewModel;
    private RefundItemAdapter itemAdapter;
    private RefundImageAdapter imageAdapter;
    private List<Uri> selectedImages = new ArrayList<>();
    private double currentRefundTotal = 0;
    private int currentSelectedCount = 0;
    private Order currentOrder;

    private final ActivityResultLauncher<Intent> imagePickerLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Intent data = result.getData();
                    if (data.getClipData() != null) {
                        int count = data.getClipData().getItemCount();
                        for (int i = 0; i < count; i++) {
                            Uri imageUri = data.getClipData().getItemAt(i).getUri();
                            if (selectedImages.size() < 10) selectedImages.add(imageUri);
                        }
                    } else if (data.getData() != null) {
                        Uri imageUri = data.getData();
                        if (selectedImages.size() < 10) selectedImages.add(imageUri);
                    }
                    imageAdapter.notifyDataSetChanged();
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRefundRequestBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = new ViewModelProvider(this).get(RefundRequestViewModel.class);
        currentOrder = (Order) getIntent().getSerializableExtra("ORDER_DATA");

        setupRecyclerViews();
        setupEventListeners();
        observeViewModel();
    }

    private void setupRecyclerViews() {
        List<OrderItem> orderItems = currentOrder.getItems();
        if (orderItems == null) orderItems = new ArrayList<>();

        itemAdapter = new RefundItemAdapter(orderItems, (totalRefund, selectedCount) -> {
            currentRefundTotal = totalRefund;
            currentSelectedCount = selectedCount;
            binding.tvEstimatedRefund.setText(String.format("%,.0fđ", totalRefund));
        });
        binding.rvRefundItems.setLayoutManager(new LinearLayoutManager(this));
        binding.rvRefundItems.setAdapter(itemAdapter);

        imageAdapter = new RefundImageAdapter(selectedImages, position -> {
            selectedImages.remove(position);
            imageAdapter.notifyItemRemoved(position);
            imageAdapter.notifyItemRangeChanged(position, selectedImages.size());
        });
        binding.rvRefundImages.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        binding.rvRefundImages.setAdapter(imageAdapter);
    }

    private void setupEventListeners() {
        binding.btnBack.setOnClickListener(v -> finish());

        binding.btnAddImage.setOnClickListener(v -> {
            if (selectedImages.size() >= 10) {
                Toast.makeText(this, "Chỉ được tải lên tối đa 10 ảnh!", Toast.LENGTH_SHORT).show();
                return;
            }
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
            imagePickerLauncher.launch(Intent.createChooser(intent, "Chọn ảnh bằng chứng"));
        });

        binding.btnSubmitRefundRequest.setOnClickListener(v -> validateAndSubmit());
    }

    private void observeViewModel() {
        viewModel.getIsLoading().observe(this, isLoading -> {
            binding.btnSubmitRefundRequest.setEnabled(!isLoading);
        });

        viewModel.getIsSubmitSuccess().observe(this, success -> {
            if (Boolean.TRUE.equals(success)) {
                Toast.makeText(this, "Gửi yêu cầu hoàn tiền thành công!", Toast.LENGTH_LONG).show();
                finish();
            }
        });

        viewModel.getErrorMessage().observe(this, msg -> {
            if (msg != null) {
                Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void validateAndSubmit() {
        Map<Integer, Integer> selectedItems = itemAdapter.getSelectedItems();
        int selectedReasonId = binding.rgCondition.getCheckedRadioButtonId();
        String detailReason = binding.edtReasonRefundRequest.getText().toString().trim();

        if (selectedItems.isEmpty()) {
            Toast.makeText(this, "Vui lòng chọn ít nhất 1 sản phẩm!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (selectedReasonId == -1) {
            Toast.makeText(this, "Vui lòng chọn tình trạng hàng!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (detailReason.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập chi tiết lí do!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (selectedImages.isEmpty()) {
            Toast.makeText(this, "Vui lòng tải lên ít nhất 1 ảnh bằng chứng!", Toast.LENGTH_SHORT).show();
            return;
        }

        String conditionValue;
        if (selectedReasonId == R.id.rbGood) {
            conditionValue = "good";
        } else if (selectedReasonId == R.id.rbDamaged) {
            conditionValue = "damaged";
        } else {
            conditionValue = "wrong_item";
        }

        viewModel.submitRefundRequest(
                currentOrder.getOrderId(),
                detailReason,
                selectedItems,
                conditionValue,
                selectedImages
        );
    }
}