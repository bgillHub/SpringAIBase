package com.bgillhub.SpringAIBase.service;

import org.springframework.ai.document.Document;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LocalSystemTools {

    @Autowired
    VectorStore vectorStore;

    @Tool(description = "Searches the internal knowledge base for relevant statements to help generate relevant content.")
    public String creativeWriting(
            @ToolParam(description = "The requested topic describing the information needed") String query) {

        // Need to specify that these search results influence the writing tool's recommendations
        List<Document> results = vectorStore.similaritySearch(
                SearchRequest.builder()
                        .query(query)
                        .topK(5)
                        .similarityThreshold(0.25)
                        .build()
        );

        if (results == null || results.isEmpty()) {
            return "No relevant documents found in the knowledge base.";
        }

        return results.stream()
                .map(Document::getText)
                .collect(Collectors.joining("\n---\n"));
    }

    @Tool(description = "Write text content to a local file, creating it if it does not exist.")
    public String writeFile(
            @ToolParam(description = "The destination file path") String filePath,
            @ToolParam(description = "The text content to write") String content) {
        try {
            Files.writeString(Path.of(filePath), content);
            return "Successfully wrote to " + filePath;
        } catch (Exception e) {
            return "Error writing file: " + e.getMessage();
        }
    }
}