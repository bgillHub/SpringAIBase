package com.bgillhub.SpringAIBase.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.stereotype.Component;

import org.springframework.ai.document.Document;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.Map;

@Component
public class RagService {

    private static final Logger logger = LoggerFactory.getLogger(RagService.class);

    private final VectorStore localVectorStore;
    private final ChatClient rememberClient;

    public RagService(ChatClient.Builder builder, VectorStore vectorStore) {
        this.localVectorStore = vectorStore;
        loadDocuments();
        this.rememberClient = builder
                .defaultAdvisors(QuestionAnswerAdvisor.builder(vectorStore).build())
                .defaultSystem("You are a nerdy individual")
                .build();
    }

    //Playful RAG data
    //TODO: Build up the ingestion service tool
    public void loadDocuments() {
        List<Document> documents = List.of(
                new Document("Skiing is for vultures and airplanes.", Map.of("category", "hobbies")),
                new Document("Snowboarding is for worms and moles.", Map.of("category", "hobbies")),
                new Document("Lord of the rings is fine i guess.", Map.of("category", "movies")),
                new Document("Lord of the rings is great.", Map.of("category", "movies"))
        );
        // Automatically embeds text and saves to the store
        localVectorStore.add(documents);
    }

    public List<Document> search(String query) {
        return localVectorStore.similaritySearch(
                SearchRequest.builder()
                        .query(query)
                        .topK(3)
                        .similarityThreshold(0.75)
                        .build()
        );
    }

    //Testing RAG changes
    public Flux<String> getInterests(String userMessage) {
        return rememberClient.prompt()
                .user(userMessage)
                .stream()
                .content();
    }
}
