package com.example.itstore.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.itstore.R;
import com.example.itstore.model.ItemNotification;

import java.util.List;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotiViewHolder> {
    private List<ItemNotification> notiList;
    private OnItemClickListener listener;

    public void setNotiList(List<ItemNotification> notiList) {
        this.notiList = notiList;

        // reset toàn bộ UI list : gọi lại getItemCount() gọi lại onBindViewHolder() cho toàn bộ
        notifyDataSetChanged();
    }
    public interface OnItemClickListener {
        void onItemClick(ItemNotification notification);
    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
    @Override
    public NotiViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notification, parent, false);
        return new NotiViewHolder(view);
    }

    @Override
    public void onBindViewHolder(NotiViewHolder holder, int position) {
        ItemNotification noti = notiList.get(position);
        if (noti == null) return;

        holder.tvNotiTitle.setText(noti.getTitle());
        holder.tvNotiContent.setText(noti.getContent());
        holder.tvNotiTime.setText(formatNotificationTime(noti.getTime()));
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(noti);
            }
        });
        if (!noti.isRead()) {
            holder.viewUnreadDot.setVisibility(View.VISIBLE);
            holder.layoutNotiContainer.setBackgroundColor(Color.parseColor("#FFF8E1"));
        } else {
            holder.viewUnreadDot.setVisibility(View.INVISIBLE);
            holder.layoutNotiContainer.setBackgroundColor(Color.WHITE);
        }

        holder.layoutNotiContainer.setOnClickListener(v -> {
            if (!noti.isRead()) {
                noti.setRead(true);

                // reset UI list item tại position : gọi lại hàm onBindViewHolder(...,position)
                notifyItemChanged(position);

            }
        });
    }

    @Override
    public int getItemCount() {
        return notiList == null ? 0 : notiList.size();
    }
    private String formatNotificationTime(String rawTime) {
        if (rawTime == null || rawTime.isEmpty()) return "";
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault());
            inputFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

            Date date = inputFormat.parse(rawTime);

            SimpleDateFormat outputFormat = new SimpleDateFormat("HH:mm - dd/MM/yyyy", Locale.getDefault());
            outputFormat.setTimeZone(TimeZone.getTimeZone("GMT+7"));

            return outputFormat.format(date);
        } catch (Exception e) {
            e.printStackTrace();
            return rawTime;
        }
    }
    public static class NotiViewHolder extends RecyclerView.ViewHolder {
        ConstraintLayout layoutNotiContainer;
        View viewUnreadDot;
        ImageView imgNotiIcon;
        TextView tvNotiTitle, tvNotiContent, tvNotiTime;

        public NotiViewHolder(View itemView) {
            super(itemView);
            layoutNotiContainer = itemView.findViewById(R.id.layoutNotiContainer);
            viewUnreadDot = itemView.findViewById(R.id.viewUnreadDot);
            imgNotiIcon = itemView.findViewById(R.id.imgNotiIcon);
            tvNotiTitle = itemView.findViewById(R.id.tvNotiTitle);
            tvNotiContent = itemView.findViewById(R.id.tvNotiContent);
            tvNotiTime = itemView.findViewById(R.id.tvNotiTime);
        }
    }

}