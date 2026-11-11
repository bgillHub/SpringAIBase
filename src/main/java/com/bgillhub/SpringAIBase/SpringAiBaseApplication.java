package com.bgillhub.SpringAIBase;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.data.redis.autoconfigure.DataRedisAutoConfiguration;
import org.springframework.boot.jdbc.autoconfigure.DataSourceAutoConfiguration;
import org.springframework.boot.session.data.redis.autoconfigure.SessionDataRedisAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class, DataRedisAutoConfiguration.class, SessionDataRedisAutoConfiguration.class})
public class SpringAiBaseApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringAiBaseApplication.class, args);
	}

}
