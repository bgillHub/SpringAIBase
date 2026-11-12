package com.bgillhub.SpringAIBase.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

import java.util.List;

//Basic Service For the General Question Prompts
@Service
public class AiCodeService {

    private final ChatClient chatClient;

    public AiCodeService(ChatClient.Builder builder) {
        this.chatClient = builder
                .defaultSystem("You are a concise, helpful assistant for our support team.")
                .build();
    }

    public String getResponse(String userMessage) {
        return chatClient.prompt()
                .user(userMessage)
                .call()
                .content();
    }

}
