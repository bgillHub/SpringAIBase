package com.bgillhub.SpringAIBase.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.api.Advisor;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.List;

//Service to handle the flirting/negging bios and responses
//NOTE: This is simply a project for humor's sake
@Service
public class AiDatingService {



    private final ChatClient interestsClient;
    private final ChatClient flirtClient;
    private final ChatClient negClient;

    private final ChatClient rememberClient;

    public record ItemDescription(String headline, String body, List<String> bullets) {}

    public AiDatingService(ChatClient.Builder builder, VectorStore vectorStore) {
        this.interestsClient = builder
                .defaultSystem("You are a concise, helpful assistant for our support team.")
                .build();
        this.flirtClient = builder
                .defaultSystem("You are a flirty individual that responds to statements.")
                .build();
        this.negClient = builder
                .defaultSystem("You are a cynical individual that responds to statements.")
                .build();

        //Playing around with RAG; need to add vector store data
        this.rememberClient = builder
                .defaultAdvisors(QuestionAnswerAdvisor.builder(vectorStore).build())
                .build();
    }

    public ItemDescription retrievePositive (List<String> hobbies, List<String> movies) {
        return interestsClient.prompt()
                .user(u -> u.text("Write a dating profile for a man with hobbies and favorite movies. Hobbies: {hobbies}. Movies: {movies}")
                        .param("hobbies", hobbies).param("movies", movies))
                .call()
                .entity(ItemDescription.class);
    }

    public Flux<String> flirt(String userMessage) {
        System.out.println("Flirting");
        return flirtClient.prompt()
                .user(userMessage)
                .stream()
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
    public Flux<String> neg(String userMessage) {
        return negClient.prompt()
                .user(userMessage.concat(". Respond very negatively"))
                .stream()
                .content();
    }

    //Testing RAG changes
    public Flux<String> getInterests(String userMessage) {
        return rememberClient.prompt()
                .user(userMessage)
                .stream()
                .content();
    }

}
