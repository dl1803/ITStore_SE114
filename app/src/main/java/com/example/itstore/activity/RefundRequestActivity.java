package com.example.itstore.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.itstore.R;
import com.example.itstore.adapter.RefundImageAdapter;
import com.example.itstore.adapter.RefundItemAdapter;
import com.example.itstore.api.RetrofitClient;
import com.example.itstore.databinding.ActivityRefundRequestBinding;
import com.example.itstore.model.Order;
import com.example.itstore.model.OrderItem;
import com.example.itstore.model.ReturnRequestResponse;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RefundRequestActivity extends AppCompatActivity {

    private ActivityRefundRequestBinding binding;
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

        currentOrder = (Order) getIntent().getSerializableExtra("ORDER_DATA");

        setupRecyclerViews();
        setupEventListeners();
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

        StringBuilder itemsJson = new StringBuilder("[");
        boolean first = true;
        for (Map.Entry<Integer, Integer> entry : selectedItems.entrySet()) {
            if (!first) itemsJson.append(",");
            itemsJson.append("{\"order_item_id\":").append(entry.getKey())
                    .append(",\"quantity\":").append(entry.getValue())
                    .append(",\"condition\":\"").append(conditionValue).append("\"}");
            first = false;
        }
        itemsJson.append("]");

        RequestBody orderIdPart = RequestBody.create(
                currentOrder.getOrderId(), MediaType.parse("text/plain"));
        RequestBody reasonPart = RequestBody.create(
                detailReason, MediaType.parse("text/plain"));
        RequestBody itemsPart = RequestBody.create(
                itemsJson.toString(), MediaType.parse("text/plain"));

        List<MultipartBody.Part> imageParts = new ArrayList<>();
        for (Uri uri : selectedImages) {
            try {
                InputStream is = getContentResolver().openInputStream(uri);
                ByteArrayOutputStream buffer = new ByteArrayOutputStream();
                byte[] temp = new byte[4096];
                int len;
                while ((len = is.read(temp)) != -1) {
                    buffer.write(temp, 0, len);
                }
                is.close();
                byte[] bytes = buffer.toByteArray();
                RequestBody rb = RequestBody.create(bytes, MediaType.parse("image/jpeg"));
                MultipartBody.Part part = MultipartBody.Part.createFormData(
                        "images", "img_" + System.currentTimeMillis() + ".jpg", rb);
                imageParts.add(part);
            } catch (Exception e) {
                Toast.makeText(this, "Lỗi đọc ảnh, vui lòng thử lại", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        binding.btnSubmitRefundRequest.setEnabled(false);

        RetrofitClient.getApiService(this)
                .createReturnRequest(orderIdPart, reasonPart, itemsPart, imageParts)
                .enqueue(new Callback<ReturnRequestResponse>() {
                    @Override
                    public void onResponse(Call<ReturnRequestResponse> call, Response<ReturnRequestResponse> response) {
                        binding.btnSubmitRefundRequest.setEnabled(true);
                        if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                            Toast.makeText(RefundRequestActivity.this,
                                    "Gửi yêu cầu hoàn tiền thành công!", Toast.LENGTH_LONG).show();
                            finish();
                        } else {
                            String msg = response.body() != null ? response.body().getMessage() : "Gửi yêu cầu thất bại";
                            Toast.makeText(RefundRequestActivity.this, msg, Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ReturnRequestResponse> call, Throwable t) {
                        binding.btnSubmitRefundRequest.setEnabled(true);
                        Toast.makeText(RefundRequestActivity.this, "Lỗi mạng", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}