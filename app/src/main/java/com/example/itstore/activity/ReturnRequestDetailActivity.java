package com.example.itstore.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.itstore.adapter.ReturnDetailImageAdapter;
import com.example.itstore.adapter.ReturnDetailItemAdapter;
import com.example.itstore.databinding.ActivityReturnRequestDetailBinding;
import com.example.itstore.model.ReturnRequestDetailResponse;
import com.example.itstore.viewmodel.ReturnRequestViewModel;

import java.text.DecimalFormat;
import java.util.ArrayList;
import androidx.recyclerview.widget.RecyclerView;

public class ReturnRequestDetailActivity extends AppCompatActivity {

    private ActivityReturnRequestDetailBinding binding;
    private ReturnRequestViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityReturnRequestDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnBack.setOnClickListener(v -> finish());

        viewModel = new ViewModelProvider(this).get(ReturnRequestViewModel.class);

        int requestId = getIntent().getIntExtra("RETURN_REQUEST_ID", -1);
        if (requestId == -1) {
            Toast.makeText(this, "Lỗi: Không tìm thấy mã yêu cầu trả hàng!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        viewModel.getReturnRequestDetail().observe(this, this::bindData);

        viewModel.getErrorMessage().observe(this, error -> {
            if (error != null) {
                Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        viewModel.fetchReturnRequestDetail(requestId);
    }

    private void bindData(ReturnRequestDetailResponse.DetailData data) {
        if (data == null) return;

        DecimalFormat formatter = new DecimalFormat("###,###,###");

        binding.tvDetailRequestId.setText("Mã YCTH: #" + data.getId());
        binding.tvDetailStatus.setText(data.getStatusVN());
        binding.tvDetailStatus.setTextColor(data.getStatusColor());
        binding.tvDetailCreatedAt.setText("Ngày tạo: " + data.getCreatedAtFormatted());
        binding.tvDetailReason.setText(data.getReason());
        binding.tvDetailRefundAmount.setText(formatter.format(data.getRefundAmount()) + "đ");

        if (data.getAdminNote() != null && !data.getAdminNote().isEmpty()) {
            binding.tvDetailAdminNoteLabel.setVisibility(View.VISIBLE);
            binding.tvDetailAdminNote.setVisibility(View.VISIBLE);
            binding.tvDetailAdminNote.setText(data.getAdminNote());
        } else {
            binding.tvDetailAdminNoteLabel.setVisibility(View.GONE);
            binding.tvDetailAdminNote.setVisibility(View.GONE);
        }

        binding.rvReturnItems.setLayoutManager(new LinearLayoutManager(this));
        binding.rvReturnItems.setAdapter(new ReturnDetailItemAdapter(
                data.getReturnItems() != null ? data.getReturnItems() : new ArrayList<>()));

        binding.rvReturnImages.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        binding.rvReturnImages.setAdapter(new ReturnDetailImageAdapter(
                data.getImages() != null ? data.getImages() : new ArrayList<>()));

        ReturnRequestDetailResponse.DetailData.ReturnAddress address = data.getAddress();
        if (address != null) {
            binding.tvDetailAddressRecipient.setText(address.getRecipient() + " - " + address.getPhoneNumber());
            binding.tvDetailAddressFull.setText(address.getStreet() + ", " + address.getWard() + ", "
                    + address.getDistrict() + ", " + address.getProvince());
        }
    }
}