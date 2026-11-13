package com.bgillhub.SpringAIBase.controller;

import com.bgillhub.SpringAIBase.service.LocalSystemTools;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.chat.client.advisor.vectorstore.VectorStoreChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.InMemoryChatMemoryRepository;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/agent")
public class LocalAgentController {

    private final ChatClient chatClient;

    public LocalAgentController(ChatClient.Builder builder, LocalSystemTools localTools, VectorStore vectorStore) {
        var chatMemory = MessageWindowChatMemory.builder()
                .chatMemoryRepository(new InMemoryChatMemoryRepository())
                .maxMessages(20)
                .build();

        this.chatClient = builder
                .defaultSystem("""
                    You are an content generation assistant. Break writing requests into steps,
                    use your available tools to create content from the context, create files, and verify your work
                    before giving a final answer. Always use the creativeWriting tool to create your writing.
                    """)
                .defaultTools(localTools)
                .defaultAdvisors(MessageChatMemoryAdvisor.builder(chatMemory).order(1).build(),
                        VectorStoreChatMemoryAdvisor.builder(vectorStore).order(2).build())
                .defaultAdvisors(advisor -> advisor.param(ChatMemory.CONVERSATION_ID, "default-conversation"))
                .build();
    }

    @PostMapping("/run")
    public String runTask(@RequestBody String userGoal) {
        return chatClient.prompt()
                .user(userGoal)
                .advisors(a -> a.param(ChatMemory.CONVERSATION_ID, "sessionId"))
                .call()
                .content();
    }

    @PostMapping("/chat")
    public String chat(
            @RequestParam(defaultValue = "default-user") String sessionId,
            @RequestBody String userMessage) {

        return chatClient.prompt()
                .user(userMessage)
                .advisors(a -> a.param(ChatMemory.CONVERSATION_ID, "sessionId"))
                .call()
                .content();
    }
}