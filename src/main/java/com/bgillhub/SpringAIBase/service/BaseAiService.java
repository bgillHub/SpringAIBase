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

    public ItemDescription retrievePositive (List<String> hobbies, List<String> movies) {

        return chatClient.prompt()
                .user(u -> u.text("Write a dating profile for a man with hobbies and favorite movies. Hobbies: {hobbies}. Movies: {movies}")
                        .param("hobbies", hobbies).param("movies", movies))
                .call()
                .entity(ItemDescription.class);
    }

    //Create a dark side of the request. As the prompt is new, the new method is needed
    public ItemDescription retrieveNegative (List<String> hobbies, List<String> movies) {

        //It turns out, the negging of each param needs to be specified
        return chatClient.prompt()
                .user(u -> u.text("Write a harsh critique of some hobbies and movies. Disliked Hobbies: {hobbies}. Disliked Movies: {movies}")
                        .param("hobbies", hobbies).param("movies", movies))
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
