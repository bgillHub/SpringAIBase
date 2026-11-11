package com.bgillhub.SpringAIBase.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BaseAiService {


    private final ChatClient chatClient;

    public record ItemDescription(String headline, String body, List<String> bullets) {}

    public BaseAiService(ChatClient.Builder builder) {
        this.chatClient = builder
                .defaultSystem("You are a concise, helpful assistant for our support team.")
                .build();
    }

    public ItemDescription retrieveItem () {

        return chatClient.prompt()
                .user(u -> u.text("Write a product description for {name}. Features: {features}")
                        .param("name", "Wireless Noise-Cancelling Headphones")
                        .param("features", "40-hour battery, ANC, USB-C"))
                .call()
                .entity(ItemDescription.class);
    }



    public String getResponse(String userMessage) {
        return chatClient.prompt()
                .user(userMessage)
                .call()
                .content();
    }
}
