package com.bgillhub.SpringAIBase.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.List;

//Service to handle the flirting/negging bios and responses
//NOTE: This is simply a project for humor's sake
@Service
public class AiDatingService {

    private final ChatClient standardClient;

    public record ItemDescription(String headline, String body, List<String> bullets) {}

    public AiDatingService(ChatClient.Builder builder) {
        this.standardClient = builder
                .defaultSystem("You are a concise individual. Never exceed 2 sentences")
                .build();
    }

    public Flux<String> flirt(String userMessage) {
        return standardClient.prompt().system("Respond in a flirtatious tone")
                .user(userMessage)
                .stream()
                .content();
    }

    //Chaotic side of chat
    public Flux<String> neg(String userMessage) {
        return standardClient.prompt().system("Respond in a cynical tone.")
                .user(userMessage)
                .stream()
                .content();
    }

    public ItemDescription retrievePositive (List<String> hobbies, List<String> movies) {
        return standardClient.prompt()
                .user(u -> u.text("Write a dating profile for a man with hobbies and favorite movies. Hobbies: {hobbies}. Movies: {movies}")
                        .param("hobbies", hobbies).param("movies", movies))
                .call()
                .entity(ItemDescription.class);
    }

    //Create the dark and weird side of the request. As the prompt is new, the new method is needed
    public ItemDescription retrieveNegative (List<String> hobbies, List<String> movies) {
        return standardClient.prompt()
                .user(u -> u.text("Write a harsh critique of some hobbies and movies. Disliked Hobbies: {hobbies}. Disliked Movies: {movies}")
                        .param("hobbies", hobbies).param("movies", movies))
                .call()
                .entity(ItemDescription.class);
    }

}
