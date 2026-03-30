package com.example.itstore.dialog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.example.itstore.databinding.DialogFilterBinding;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class FilterProductDialog extends BottomSheetDialogFragment {

    private DialogFilterBinding binding;
    private OnFilterAppliedListener listener;
    public interface OnFilterAppliedListener {
        void onFilterApplied(double minPrice, double maxPrice, String brand);
    }
    public void setOnFilterAppliedListener(OnFilterAppliedListener listener) {
        this.listener = listener;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DialogFilterBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.btnApplyFilter.setOnClickListener(v -> {
            String minStr = binding.edtMinPrice.getText().toString().trim();
            String maxStr = binding.edtMaxPrice.getText().toString().trim();
            double min = minStr.isEmpty() ? 0 : Double.parseDouble(minStr);
            double max = maxStr.isEmpty() ? Double.MAX_VALUE : Double.parseDouble(maxStr);
            if (listener != null) {
                listener.onFilterApplied(min, max, "Tất cả");
            }
            dismiss();
        });
        binding.btnResetFilter.setOnClickListener(v -> {
            binding.edtMinPrice.setText("");
            binding.edtMaxPrice.setText("");
        });
        binding.btnCloseFilter.setOnClickListener(v -> dismiss());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
