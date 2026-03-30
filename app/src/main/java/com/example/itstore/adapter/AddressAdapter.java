package com.example.itstore.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.itstore.R;
import com.example.itstore.model.Address;

import java.util.List;

public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.AddressViewHolder> {

    private List<Address> addressList;

    public interface OnItemClickListener{
        void OnEditClick(Address address);
        void OnDeleteClick(Address address);
    }

    private OnItemClickListener listener;

    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }

    public void setAddressList(List<Address> addressList) {
        this.addressList = addressList;
        notifyDataSetChanged();
    };


    @NonNull
    @Override
    public AddressViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_address, parent, false);
        return new AddressViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AddressViewHolder holder, int position) {
        Address address = addressList.get(position);
        if (address == null) return;

        holder.tvNameAndPhone.setText(address.getRecipient() + " | " + address.getPhoneNumber());

        String fullAddress = address.getStreet() + ", " + address.getWard() + ", " + address.getDistrict() + ", " + address.getProvince();
        holder.tvAddressDetail.setText(fullAddress);

        if (address.isDefault()) {
            holder.tvDefault.setVisibility(View.VISIBLE);
        } else {
            holder.tvDefault.setVisibility(View.GONE);
        }

        holder.tvEdit.setOnClickListener(v -> {
            if (listener != null){
                listener.OnEditClick(address);
            }
        });

        holder.tvDelete.setOnClickListener(v -> {
            if (listener != null){
                listener.OnDeleteClick(address);
            }
        });
    }

    @Override
    public int getItemCount() {
        return addressList == null ? 0 : addressList.size();
    }

    public static class AddressViewHolder extends RecyclerView.ViewHolder {
        TextView tvNameAndPhone, tvAddressDetail, tvDefault, tvEdit, tvDelete;

        public AddressViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNameAndPhone = itemView.findViewById(R.id.tvNameAndPhone);
            tvAddressDetail = itemView.findViewById(R.id.tvAddressDetail);
            tvDefault = itemView.findViewById(R.id.tvDefault);
            tvEdit = itemView.findViewById(R.id.tvEdit);
            tvDelete = itemView.findViewById(R.id.tvDelete);
        }
    }
}
