package com.szj.chatgpt.api;

import com.szj.chatgpt.model.ChatRequest;
import com.szj.chatgpt.model.ChatResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface OpenAIService {

    @Headers({
            "Content-Type: application/json"
    })
    @POST("v1/chat/completions")
    Call<ChatResponse> createChatCompletion(@Body ChatRequest request);
}
