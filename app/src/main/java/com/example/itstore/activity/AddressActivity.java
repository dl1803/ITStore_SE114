package com.example.itstore.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.itstore.R;
import com.example.itstore.adapter.AddressAdapter;
import com.example.itstore.databinding.ActivityAddressListBinding;
import com.example.itstore.model.Address;
import com.example.itstore.viewmodel.AddressViewModel;


public class AddressActivity extends AppCompatActivity {
    private AddressViewModel addressViewModel;
    private ActivityAddressListBinding binding;
    private AddressAdapter addressAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        binding = ActivityAddressListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        addressAdapter = new AddressAdapter();
        binding.rvAddressList.setLayoutManager(new LinearLayoutManager(this));
        binding.rvAddressList.setAdapter(addressAdapter);

        addressViewModel = new ViewModelProvider(this).get(AddressViewModel.class);

        addressViewModel.getAddressList().observe(this, addresses -> {
            if (addresses != null && !addresses.isEmpty()) {

                binding.rvAddressList.setVisibility(View.VISIBLE);
                binding.layoutEmptyState.getRoot().setVisibility(View.GONE);

                addressAdapter.setAddressList(addresses);
            } else {

                binding.rvAddressList.setVisibility(View.GONE);
                binding.layoutEmptyState.getRoot().setVisibility(View.VISIBLE);

                binding.layoutEmptyState.tvEmptyMessage.setText("Bạn chưa có địa chỉ giao hàng nào. Thêm ngay nhé!");
            }
        });

        addressViewModel.getErrorMessage().observe(this, error -> {
            if (error != null) {
                Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
            }
        });

        addressViewModel.getDeleteMessage().observe(this, message -> {
            if (message != null) {
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
            }
        });

        addressViewModel.getDeleteSuccess().observe(this, isSuccess -> {
            if (isSuccess != null && isSuccess) {
                addressViewModel.getAddressList();
            }
        });

        binding.btnBack.setOnClickListener(v -> finish());


        addressAdapter.setOnItemClickListener(new AddressAdapter.OnItemClickListener() {
            @Override
            public void OnEditClick(Address address) {
                Intent intent = new Intent(AddressActivity.this, AddEditAddressActivity.class);
                intent.putExtra("ADDRESS_ID", address.getId());
                intent.putExtra("NAME", address.getRecipient());
                intent.putExtra("PHONE", address.getPhoneNumber());
                intent.putExtra("PROVINCE", address.getProvince());
                intent.putExtra("DISTRICT", address.getDistrict());
                intent.putExtra("WARD", address.getWard());
                intent.putExtra("STREET", address.getStreet());
                intent.putExtra("IS_DEFAULT", address.isDefault());
                startActivity(intent);
            }

            @Override
            public void OnDeleteClick(Address address) {
                new androidx.appcompat.app.AlertDialog.Builder(AddressActivity.this)
                        .setTitle("Xóa địa chỉ")
                        .setMessage("Bạn có chắc chắn muốn xóa địa chỉ này không?")
                        .setPositiveButton("Xóa", (dialog, which) -> {

                            addressViewModel.deleteAddress(AddressActivity.this, address.getId());

                        })
                        .setNegativeButton("Hủy", null)
                        .show();
            }
        });

        binding.btnAddAddress.setOnClickListener(v -> {
            Intent intent = new Intent(AddressActivity.this, AddEditAddressActivity.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onResume(){
        super.onResume();
        addressViewModel.fetchAddresses(this);
    }
}