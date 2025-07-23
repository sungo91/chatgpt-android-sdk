package com.example.chatgpt;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.szj.chatgpt.model.ChatMessage;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

   private final List<ChatMessage> messageList;

   public ChatAdapter(List<ChatMessage> messages) {
      this.messageList = messages;
   }

   @Override
   public int getItemViewType(int position) {
      return "user".equals(messageList.get(position).getRole()) ? 1 : 0;
   }

   @Override
   public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
      int layout = (viewType == 1)
              ? R.layout.item_chat_right
              : R.layout.item_chat_left;
      View view = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
      return new MessageHolder(view);
   }

   @Override
   public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
      ((MessageHolder) holder).bind(messageList.get(position).getContent());
   }

   @Override
   public int getItemCount() {
      return messageList.size();
   }

   static class MessageHolder extends RecyclerView.ViewHolder {
      TextView textView;

      public MessageHolder(View itemView) {
         super(itemView);
         textView = itemView.findViewById(R.id.textViewMessage);
      }

      void bind(String text) {
         textView.setText(text);
      }
   }
}
