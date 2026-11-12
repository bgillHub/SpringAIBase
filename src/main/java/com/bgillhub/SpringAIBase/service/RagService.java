package com.bgillhub.SpringAIBase.service;

import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import org.springframework.ai.document.Document;
import java.util.List;
import java.util.Map;

@Component
public class RagService {


    private final VectorStore vectorStore;

    public RagService(VectorStore vectorStore) {
        this.vectorStore = vectorStore;
        loadDocuments();
    }

    //Playful RAG data
    public void loadDocuments() {
        List<Document> documents = List.of(
                new Document("Skiing is for vultures and airplanes.", Map.of("category", "hobbies")),
                new Document("Snowboarding is for worms and moles.", Map.of("category", "hobbies")),
                new Document("Lord of the rings is fine i guess.", Map.of("category", "movies")),
                new Document("Lord of the rings is great.", Map.of("category", "movies"))
        );
        // Automatically embeds text and saves to the store
        vectorStore.add(documents);
    }

    public List<Document> search(String query) {
        return vectorStore.similaritySearch(
                SearchRequest.builder()
                        .query(query)
                        .topK(3)
                        .similarityThreshold(0.75)
                        .build()
        );
    }

    //TODO: Build up the ingestion service tool; which would run on xyz occurrences
}
