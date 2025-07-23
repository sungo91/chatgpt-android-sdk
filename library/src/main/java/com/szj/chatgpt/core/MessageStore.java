package com.szj.chatgpt.core;

import com.szj.chatgpt.model.ChatMessage;

import java.util.LinkedList;
import java.util.List;

public class MessageStore {

   private static final int MAX_MESSAGE_COUNT = 10;

   private final LinkedList<ChatMessage> messageList;

   public MessageStore() {
      messageList = new LinkedList<>();
   }

   public void addUserMessage(String content) {
      addMessage(new ChatMessage("user", content));
   }

   public void addAssistantMessage(String content) {
      addMessage(new ChatMessage("assistant", content));
   }

   private void addMessage(ChatMessage message) {
      if (messageList.size() >= MAX_MESSAGE_COUNT) {
         messageList.removeFirst();
      }
      messageList.add(message);
   }

   public List<ChatMessage> getMessageHistory() {
      return new LinkedList<>(messageList); // 返回副本，避免被修改
   }

   public void clear() {
      messageList.clear();
   }

   public void setMessageHistory(List<ChatMessage> history) {
      messageList.clear();
      messageList.addAll(history);
   }
}
