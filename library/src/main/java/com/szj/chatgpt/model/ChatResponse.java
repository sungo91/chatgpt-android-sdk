package com.szj.chatgpt.model;

import java.util.List;

public class ChatResponse {

    private List<Choice> choices;

    public ChatMessage getFirstAssistantMessage() {
        if (choices != null && !choices.isEmpty()) {
            return choices.get(0).message;
        }
        return null;
    }

    public static class Choice {
        public ChatMessage message;
    }
}