package com.szj.chatgpt.core;

import android.content.Context;
import android.util.Log;

import com.szj.chatgpt.api.OpenAIService;
import com.szj.chatgpt.model.ChatMessage;
import com.szj.chatgpt.model.ChatRequest;
import com.szj.chatgpt.model.ChatResponse;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.*;
import retrofit2.converter.gson.GsonConverterFactory;

public class ChatGPTClient {

    private static ChatGPTClient instance;
    private OpenAIService api;
    private String apiKey;

    private MessageStore messageStore = new MessageStore();

    private List<ChatMessage> messageHistory = new ArrayList<>();

    private LocalCacheManager cacheManager;
    private Context appContext;

    private ChatGPTClient(String apiKey, String baseUrl, Context context) {
        this.apiKey = apiKey;
        this.appContext = context;

        // 初始化缓存和历史记录
        cacheManager = new LocalCacheManager(appContext);
        messageStore = new MessageStore();
        messageStore.setMessageHistory(cacheManager.loadMessages());

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(chain -> {
                    return chain.proceed(
                            chain.request().newBuilder()
                                    .addHeader("Authorization", "Bearer " + apiKey)
                                    .build()
                    );
                })
                .addInterceptor(logging)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        api = retrofit.create(OpenAIService.class);
    }

    public static void init(String apiKey, String baseUrl, Context context) {
        if (instance == null) {
            instance = new ChatGPTClient(apiKey, baseUrl, context.getApplicationContext());
        }
    }

    public static ChatGPTClient getInstance() {
        if (instance == null) {
            throw new IllegalStateException("ChatGPTClient not initialized");
        }
        return instance;
    }

    public void sendMessage(String userMessage, Callback<ChatResponse> callback) {
        messageStore.addUserMessage(userMessage);

        ChatRequest request = new ChatRequest();
        request.setModel("gpt-3.5-turbo");
        request.setMessages(messageStore.getMessageHistory());

        Call<ChatResponse> call = api.createChatCompletion(request);
        call.enqueue(new Callback<ChatResponse>() {
            @Override
            public void onResponse(Call<ChatResponse> call, Response<ChatResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ChatMessage reply = response.body().getFirstAssistantMessage();
                    if (reply != null) {
                        messageStore.addAssistantMessage(reply.getContent());
                    }

                    // 👉 保存对话到本地
                    cacheManager.saveMessages(messageStore.getMessageHistory());

                    callback.onResponse(call, response);
                } else {
                    callback.onFailure(call, new Throwable("API error"));
                }
            }

            @Override
            public void onFailure(Call<ChatResponse> call, Throwable t) {
                callback.onFailure(call, t);
            }
        });
    }

    public MessageStore getMessageStore() {
        return messageStore;
    }

    public void clearConversation() {
        messageStore.clear();
        cacheManager.clearMessages();
    }
}