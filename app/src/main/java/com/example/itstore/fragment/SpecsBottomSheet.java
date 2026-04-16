package com.example.itstore.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.itstore.R;
import com.example.itstore.model.Product;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class SpecsBottomSheet extends BottomSheetDialogFragment {

    private Product product;

    public SpecsBottomSheet(Product product) {
        this.product = product;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.layout_bottom_sheet_specs, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextView tvDescription = view.findViewById(R.id.tvDescription);
        if (product != null) {
            String desc = product.getDescription();
            if (desc != null && !desc.isEmpty()) {
                tvDescription.setText(desc);
            } else {
                tvDescription.setText("Đang cập nhật thông tin cấu hình...");
            }
        }
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme);
    }
}