package com.bgillhub.SpringAIBase.config;

import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.vectorstore.redis.RedisVectorStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import redis.clients.jedis.JedisPooled;
import redis.clients.jedis.RedisClient;

import java.util.List;

@Configuration
public class RedisConfig {
    @Bean
    public JedisConnectionFactory redisConnectionFactory() {
        return new JedisConnectionFactory();
    }

    @Bean
    public RedisTemplate<String, Message> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Message> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new StringRedisSerializer());
        return template;
    }

    @Bean
    public RedisClient jedisPooled() {
        // Connect using host and port (or pass URI/credentials)
        return new RedisClient.Builder().build();
    }

    @Bean
    public RedisVectorStore vectorStore(RedisClient jedisPooled, EmbeddingModel embeddingModel) {
        return RedisVectorStore.builder(jedisPooled, embeddingModel)
                .indexName("spring-ai-index")
                .prefix("spring-ai:")
                .metadataFields(
                        RedisVectorStore.MetadataField.text("conversationId") // Required for Chat Memory
                )
                .initializeSchema(true)
                .build();
    }

}
