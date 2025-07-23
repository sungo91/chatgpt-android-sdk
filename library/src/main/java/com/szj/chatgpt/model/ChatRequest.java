package com.szj.chatgpt.model;

import java.util.List;

public class ChatRequest {
    private String model;
    private List<ChatMessage> messages;

    public void setModel(String model) {
        this.model = model;
    }

    public void setMessages(List<ChatMessage> messages) {
        this.messages = messages;
    }
}