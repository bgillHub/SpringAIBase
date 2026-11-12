package com.bgillhub.SpringAIBase.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

import java.util.List;

//Service to handle the flirting/negging bios and responses
@Service
public class AiDatingService {


    private final ChatClient interestsClient;
    private final ChatClient flirtClient;
    private final ChatClient negClient;

    public record ItemDescription(String headline, String body, List<String> bullets) {}

    public AiDatingService(ChatClient.Builder builder) {
        this.interestsClient = builder
                .defaultSystem("You are a concise, helpful assistant for our support team.")
                .build();
        this.flirtClient = builder
                .defaultSystem("You are a flirty individual that responds to statements.")
                .build();
        this.negClient = builder
                .defaultSystem("You are a cynical individual that responds to statements.")
                .build();
    }

    public ItemDescription retrievePositive (List<String> hobbies, List<String> movies) {

        return interestsClient.prompt()
                .user(u -> u.text("Write a dating profile for a man with hobbies and favorite movies. Hobbies: {hobbies}. Movies: {movies}")
                        .param("hobbies", hobbies).param("movies", movies))
                .call()
                .entity(ItemDescription.class);
    }

    public String flirt(String userMessage) {
        return flirtClient.prompt()
                .user(userMessage)
                .call()
                .content();
    }

    //Create the dark and weird side of the request. As the prompt is new, the new method is needed
    public ItemDescription retrieveNegative (List<String> hobbies, List<String> movies) {

        //It turns out, the negging of each param needs to be specified
        return interestsClient.prompt()
                .user(u -> u.text("Write a harsh critique of some hobbies and movies. Disliked Hobbies: {hobbies}. Disliked Movies: {movies}")
                        .param("hobbies", hobbies).param("movies", movies))
                .call()
                .entity(ItemDescription.class);
    }

    //Chaotic side of chat
    public String neg(String userMessage) {
        return negClient.prompt()
                .user(userMessage)
                .call()
                .content();
    }
}
