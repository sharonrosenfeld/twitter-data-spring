package com.sharon.twittsstorage.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sharon.twittsstorage.dao.es.AnalyzedTwittsDao;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class ApplicationConfig {

    @Bean
    public AnalyzedTwittsDao analyzedTwittsDao(RestHighLevelClient client, ObjectMapper objectMapper) {
        return new AnalyzedTwittsDao(client, objectMapper);
    }

    @Bean
    ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

}
