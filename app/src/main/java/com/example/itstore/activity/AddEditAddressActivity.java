package com.example.itstore.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.itstore.R;
import com.example.itstore.databinding.ActivityAddressInfoBinding;
import com.example.itstore.model.AddressRequest;
import com.example.itstore.viewmodel.AddEditAddressViewModel;

public class AddEditAddressActivity extends AppCompatActivity {

    private ActivityAddressInfoBinding binding;
    private AddEditAddressViewModel addEditAddressViewModel;

    private boolean isEditMode = false;
    private int curAddressId = -1;

    private final String[] fakeProvinces = {"TP.Hồ Chí Minh", "Hà Nội", "Đà Nẵng", "Cần Thơ"};
    private final String[] fakeDistricts = {"Quận 1", "Quận 3", "Quận Bình Thạnh", "Thủ Đức"};
    private final String[] fakeWards = {"Phường Bến Nghé", "Phường 30", "Phường 25", "Linh Trung"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        binding = ActivityAddressInfoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnBack.setOnClickListener(v -> finish());

        addEditAddressViewModel = new ViewModelProvider(this).get(AddEditAddressViewModel.class);

        addEditAddressViewModel.getMessage().observe(this, message -> {
            if (message != null) {
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
            }
        });

        addEditAddressViewModel.getAddSuccess().observe(this, isSuccess -> {
            if (isSuccess != null && isSuccess) {
                finish();
            }
        });


        setupUI();

        handleDropdownClick();

        binding.btnSaveAddress.setOnClickListener(v -> {
            if (validateData()){
                String name = binding.edtName.getText().toString().trim();
                String phone = binding.edtPhone.getText().toString().trim();
                String province = binding.edtProvince.getText().toString().trim();
                String district = binding.edtDistrict.getText().toString().trim();
                String ward = binding.edtWard.getText().toString().trim();
                String street = binding.edtStreet.getText().toString().trim();
                if (isEditMode){
                    Toast.makeText(this, "Api Put Address", Toast.LENGTH_SHORT).show();
                } else {
                    AddressRequest request = new AddressRequest(name, phone, province, district, ward, street);
                    addEditAddressViewModel.addAddress(this, request);
                }
            }
        });

    }
    public void setupUI(){
        Intent intent = getIntent();

        if (intent.hasExtra("ADDRESS_ID")){
            isEditMode = true;
            curAddressId = intent.getIntExtra("ADDRESS_ID", -1);

            binding.tvHeaderTitle.setText("Cập nhật địa chỉ");
            binding.btnSaveAddress.setText("Cập nhật");

            String name = intent.getStringExtra("NAME");
            String phone = intent.getStringExtra("PHONE");
            String province = intent.getStringExtra("PROVINCE");
            String district = intent.getStringExtra("DISTRICT");
            String ward = intent.getStringExtra("WARD");
            String street = intent.getStringExtra("STREET");
            boolean isDefault = intent.getBooleanExtra("IS_DEFAULT", false);

            if ("Chưa cập nhật".equals(province)){
                binding.edtName.setText("");
                binding.edtPhone.setText("");
                binding.edtProvince.setText("");
                binding.edtDistrict.setText("");
                binding.edtWard.setText("");
                binding.edtStreet.setText("");
            } else {
                binding.edtName.setText(name);
                binding.edtPhone.setText(phone);
                binding.edtProvince.setText(province);
                binding.edtDistrict.setText(district);
                binding.edtWard.setText(ward);
                binding.edtStreet.setText(street);
            }
            binding.switchDefault.setChecked(isDefault);

        } else {
            isEditMode = false;

        }
    }

    private void handleDropdownClick() {
        binding.edtProvince.setOnClickListener(v -> {
            new androidx.appcompat.app.AlertDialog.Builder(this)
                    .setTitle("Chọn Tỉnh / Thành phố")
                    .setItems(fakeProvinces, (dialog, which) -> {
                        String selectedProvince = fakeProvinces[which];
                        binding.edtProvince.setText(selectedProvince);
                        binding.edtDistrict.setText("");
                        binding.edtWard.setText("");
                    }).show();
        });

        binding.edtDistrict.setOnClickListener(v -> {
            if (binding.edtProvince.getText().toString().isEmpty()) {
                Toast.makeText(this, "Vui lòng chọn Tỉnh/Thành phố trước!", Toast.LENGTH_SHORT).show();
                return;
            }

            new androidx.appcompat.app.AlertDialog.Builder(this)
                    .setTitle("Chọn Quận / Huyện")
                    .setItems(fakeDistricts, (dialog, which) -> {
                        String selectedDistrict = fakeDistricts[which];
                        binding.edtDistrict.setText(selectedDistrict);
                        binding.edtWard.setText("");
                    }).show();
        });

        binding.edtWard.setOnClickListener(v -> {
            if (binding.edtDistrict.getText().toString().isEmpty()) {
                Toast.makeText(this, "Vui lòng chọn Quận/Huyện trước!", Toast.LENGTH_SHORT).show();
                return;
            }

            new androidx.appcompat.app.AlertDialog.Builder(this)
                    .setTitle("Chọn Phường/Xã")
                    .setItems(fakeWards, (dialog, which) -> {
                        String selectedWard = fakeWards[which];
                        binding.edtWard.setText(selectedWard);
                    })
                    .show();
        });
    }


    private boolean validateData() {
        String name = binding.edtName.getText().toString().trim();
        String phone = binding.edtPhone.getText().toString().trim();
        String province = binding.edtProvince.getText().toString().trim();
        String district = binding.edtDistrict.getText().toString().trim();
        String ward = binding.edtWard.getText().toString().trim();
        String street = binding.edtStreet.getText().toString().trim();

        boolean isValid = true;

        if (name.isEmpty()) {
            binding.edtName.setError("Vui lòng nhập họ tên người nhận");
            isValid = false;
        }

        if (phone.isEmpty()) {
            binding.edtPhone.setError("Vui lòng nhập số điện thoại");
            isValid = false;
        } else if (!phone.matches("^(03|05|07|08|09)\\d{8}$")) {
            binding.edtPhone.setError("Số điện thoại không hợp lệ!");
            isValid = false;
        }

        if (province.isEmpty() || district.isEmpty() || ward.isEmpty() || street.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ địa chỉ giao hàng!", Toast.LENGTH_SHORT).show();
            isValid = false;
        }

        return isValid;
    }
}