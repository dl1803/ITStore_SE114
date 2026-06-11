package com.example.itstore.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.itstore.R;
import com.example.itstore.model.ChatMessage;
import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder> {

    private List<ChatMessage> messageList;

    public ChatAdapter(List<ChatMessage> messageList) {
        this.messageList = messageList;
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_message, parent, false);
        return new ChatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
        ChatMessage message = messageList.get(position);
        if (message.isUser()) {
            holder.layoutUserMessage.setVisibility(View.VISIBLE);
            holder.layoutAiMessage.setVisibility(View.GONE);
            holder.tvUserText.setText(message.getText());
        } else {
            holder.layoutAiMessage.setVisibility(View.VISIBLE);
            holder.layoutUserMessage.setVisibility(View.GONE);
            holder.tvAiText.setText(message.getText());
        }
    }

    @Override
    public int getItemCount() {
        return messageList != null ? messageList.size() : 0;
    }

    public void streamToken(RecyclerView recyclerView, int position, String token, String fullText) {
        if (position >= 0 && position < messageList.size()) {
            messageList.set(position, new ChatMessage(fullText, false));
        }
        RecyclerView.ViewHolder holder = recyclerView.findViewHolderForAdapterPosition(position);
        if (holder instanceof ChatViewHolder) {
            ((ChatViewHolder) holder).tvAiText.append(token);
        } else {
            notifyItemChanged(position);
        }
    }

    public static class ChatViewHolder extends RecyclerView.ViewHolder {
        LinearLayout layoutUserMessage, layoutAiMessage;
        TextView tvUserText, tvAiText;

        public ChatViewHolder(@NonNull View itemView) {
            super(itemView);
            layoutUserMessage = itemView.findViewById(R.id.layoutUserContainer);
            layoutAiMessage = itemView.findViewById(R.id.layoutAiContainer);
            tvUserText = itemView.findViewById(R.id.tvUserText);
            tvAiText = itemView.findViewById(R.id.tvAiText);
        }
    }
}