package com.szj.chatgpt.model;

public class ChatMessage {
   private String role; // "user", "assistant", "system"
   private String content;

   public ChatMessage(String role, String content) {
      this.role = role;
      this.content = content;
   }

   public String getRole() { return role; }
   public String getContent() { return content; }
}
