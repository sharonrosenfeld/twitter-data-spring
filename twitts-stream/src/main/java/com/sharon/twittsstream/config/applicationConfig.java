package com.sharon.twittsstream.config;

import com.sharon.twittsstream.service.KafkaQueueService;
import com.sharon.twittsstream.service.PersistentQueueService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class applicationConfig {

    @Bean("KafkaQueueService")
    public PersistentQueueService persistentQueueService() {
        return new KafkaQueueService();
    }
}
