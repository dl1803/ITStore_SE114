package com.example.itstore.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.itstore.R;
import com.example.itstore.api.GhnApiClient;
import com.example.itstore.databinding.ActivityAddressInfoBinding;
import com.example.itstore.model.AddressRequest;
import com.example.itstore.model.GhnDistrict;
import com.example.itstore.model.GhnProvince;
import com.example.itstore.model.GhnResponse;
import com.example.itstore.model.GhnWard;
import com.example.itstore.viewmodel.AddEditAddressViewModel;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;

public class AddEditAddressActivity extends AppCompatActivity {

    private ActivityAddressInfoBinding binding;
    private AddEditAddressViewModel addEditAddressViewModel;

    private boolean isEditMode = false;
    private int curAddressId = -1;

    private final String GHN_TOKEN = "5369cdb2-3fd4-11f1-b84f-e215adfdd13e";

    private List<GhnProvince> provinceList = new ArrayList<>();
    private List<GhnDistrict> districtList = new ArrayList<>();
    private List<GhnWard> wardList = new ArrayList<>();

    private int selectedProvinceId = -1;
    private int selectedDistrictId = -1;
    private String selectedWardCode = "";

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
                if (binding.switchDefault.isChecked()){
                    int targetId;
                    if (isEditMode){
                        targetId = curAddressId;
                    } else {
                        targetId = addEditAddressViewModel.getNewAddressId().getValue();
                    }
                    addEditAddressViewModel.setDefaultAddress(this, targetId);
                } else {
                    finish();
                }
            }
        });

        addEditAddressViewModel.getSetDefaultSuccess().observe(this, isSuccess -> {
            if (isSuccess != null && isSuccess) {
                finish();
            }
        });


        setupUI();
        fetchProvinces();

        handleDropdownClick();

        binding.btnSaveAddress.setOnClickListener(v -> {
            if (validateData()){
                String name = binding.edtName.getText().toString().trim();
                String phone = binding.edtPhone.getText().toString().trim();
                String province = binding.edtProvince.getText().toString().trim();
                String district = binding.edtDistrict.getText().toString().trim();
                String ward = binding.edtWard.getText().toString().trim();
                String street = binding.edtStreet.getText().toString().trim();
                AddressRequest request = new AddressRequest(name, phone, province, district, ward, street);

                if (isEditMode){
                    addEditAddressViewModel.updateAddress(this, curAddressId, request);
                } else {
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
            if (provinceList.isEmpty()) {
                Toast.makeText(this, "Đang tải dữ liệu, đợi xíu sếp ơi...", Toast.LENGTH_SHORT).show();
                return;
            }

            String[] provinceNames = new String[provinceList.size()];
            for (int i = 0; i < provinceList.size(); i++) provinceNames[i] = provinceList.get(i).getProvinceName();

            new AlertDialog.Builder(this)
                    .setTitle("Chọn Tỉnh / Thành phố")
                    .setItems(provinceNames, (dialog, which) -> {
                        GhnProvince selected = provinceList.get(which);
                        binding.edtProvince.setText(selected.getProvinceName());

                        binding.edtDistrict.setText("");
                        binding.edtWard.setText("");
                        districtList.clear();
                        wardList.clear();

                        selectedProvinceId = selected.getProvinceID();
                        fetchDistricts(selectedProvinceId);
                    }).show();
        });

        binding.edtDistrict.setOnClickListener(v -> {
            if (selectedProvinceId == -1 || districtList.isEmpty()) {
                Toast.makeText(this, "Vui lòng chọn Tỉnh/Thành phố trước!", Toast.LENGTH_SHORT).show();
                return;
            }
            String[] districtNames = new String[districtList.size()];
            for (int i = 0; i < districtList.size(); i++) districtNames[i] = districtList.get(i).getDistrictName();

            new AlertDialog.Builder(this)
                    .setTitle("Chọn Quận / Huyện")
                    .setItems(districtNames, (dialog, which) -> {
                        GhnDistrict selected = districtList.get(which);
                        binding.edtDistrict.setText(selected.getDistrictName());

                        binding.edtWard.setText("");
                        wardList.clear();

                        selectedDistrictId = selected.getDistrictID();
                        fetchWards(selectedDistrictId);
                    }).show();
        });

        binding.edtWard.setOnClickListener(v -> {
            if (selectedDistrictId == -1 || wardList.isEmpty()) {
                Toast.makeText(this, "Vui lòng chọn Quận/Huyện trước!", Toast.LENGTH_SHORT).show();
                return;
            }

            String[] wardNames = new String[wardList.size()];
            for (int i = 0; i < wardList.size(); i++) wardNames[i] = wardList.get(i).getWardName();

            new AlertDialog.Builder(this)
                    .setTitle("Chọn Phường/Xã")
                    .setItems(wardNames, (dialog, which) -> {
                        GhnWard selected = wardList.get(which);
                        binding.edtWard.setText(selected.getWardName());
                        selectedWardCode = selected.getWardCode();
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

    private void fetchProvinces() {
        GhnApiClient.getApiService().getProvinces(GHN_TOKEN).enqueue(new retrofit2.Callback<GhnResponse<List<GhnProvince>>>() {
            @Override
            public void onResponse(Call<GhnResponse<List<GhnProvince>>> call, retrofit2.Response<GhnResponse<List<GhnProvince>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    provinceList = response.body().getData();

                    // Collator là công cụ so sánh chuỗi theo ngôn ngữ xác định
                    Collator collator = Collator.getInstance(new Locale("vi", "VN"));
                    Collections.sort(provinceList, (p1, p2) -> collator.compare(p1.getProvinceName(), p2.getProvinceName()));
                }
            }
            @Override public void onFailure(Call<GhnResponse<List<GhnProvince>>> call, Throwable t) {}
        });
    }

    private void fetchDistricts(int provinceId) {
        GhnApiClient.getApiService().getDistricts(GHN_TOKEN, provinceId).enqueue(new retrofit2.Callback<GhnResponse<List<GhnDistrict>>>() {
            @Override
            public void onResponse(Call<GhnResponse<List<GhnDistrict>>> call, retrofit2.Response<GhnResponse<List<GhnDistrict>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    districtList = response.body().getData();
                    Collator collator = Collator.getInstance(new Locale("vi", "VN"));
                    Collections.sort(districtList, (d1, d2) -> collator.compare(d1.getDistrictName(), d2.getDistrictName()));
                }
            }
            @Override public void onFailure(Call<GhnResponse<List<GhnDistrict>>> call, Throwable t) {}
        });
    }

    private void fetchWards(int districtId) {
        GhnApiClient.getApiService().getWards(GHN_TOKEN, districtId).enqueue(new retrofit2.Callback<GhnResponse<List<GhnWard>>>() {
            @Override
            public void onResponse(Call<GhnResponse<List<GhnWard>>> call, retrofit2.Response<GhnResponse<List<GhnWard>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    wardList = response.body().getData();
                    Collator collator = Collator.getInstance(new Locale("vi", "VN"));
                    Collections.sort(wardList, (w1, w2) -> collator.compare(w1.getWardName(), w2.getWardName()));
                }
            }
            @Override public void onFailure(Call<GhnResponse<List<GhnWard>>> call, Throwable t) {}
        });
    }
}