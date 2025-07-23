package com.szj.chatgpt.core;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.szj.chatgpt.model.ChatMessage;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class LocalCacheManager {

   private static final String PREF_NAME = "chatgpt_cache";
   private static final String KEY_MESSAGES = "messages";

   private final SharedPreferences prefs;
   private final Gson gson;

   public LocalCacheManager(Context context) {
      prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
      gson = new Gson();
   }

   public void saveMessages(List<ChatMessage> messages) {
      String json = gson.toJson(messages);
      prefs.edit().putString(KEY_MESSAGES, json).apply();
   }

   public List<ChatMessage> loadMessages() {
      String json = prefs.getString(KEY_MESSAGES, null);
      if (json == null) return new ArrayList<>();

      Type type = new TypeToken<List<ChatMessage>>() {}.getType();
      try {
         return gson.fromJson(json, type);
      } catch (Exception e) {
         return new ArrayList<>();
      }
   }

   public void clearMessages() {
      prefs.edit().remove(KEY_MESSAGES).apply();
   }
}
