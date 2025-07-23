package com.example.chatgpt;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.szj.chatgpt.core.ChatGPTClient;
import com.szj.chatgpt.model.ChatMessage;
import com.szj.chatgpt.model.ChatResponse;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private EditText editText;
    private ChatAdapter adapter;
    private List<ChatMessage> messages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 初始化 SDK
        ChatGPTClient.init("your-api-key", "https://api.openai.com/", getApplicationContext());
        

        messages = new ArrayList<>(ChatGPTClient.getInstance().getMessageStore().getMessageHistory());

        recyclerView = findViewById(R.id.recyclerView);
        editText = findViewById(R.id.editTextMessage);
        Button sendBtn = findViewById(R.id.buttonSend);

        adapter = new ChatAdapter(messages);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        sendBtn.setOnClickListener(v -> {
            String input = editText.getText().toString().trim();
            if (!input.isEmpty()) {
                editText.setText("");
                appendMessage("user", input);

                ChatGPTClient.getInstance().sendMessage(input, new Callback<ChatResponse>() {
                    @Override
                    public void onResponse(Call<ChatResponse> call, Response<ChatResponse> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            ChatMessage reply = response.body().getFirstAssistantMessage();
                            if (reply != null) {
                                runOnUiThread(() -> appendMessage("assistant", reply.getContent()));
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ChatResponse> call, Throwable t) {
                        runOnUiThread(() -> appendMessage("assistant", "Network error."));
                    }
                });
            }
        });
    }

    private void appendMessage(String role, String content) {
        messages.add(new ChatMessage(role, content));
        adapter.notifyItemInserted(messages.size() - 1);
        recyclerView.scrollToPosition(messages.size() - 1);
    }
}

