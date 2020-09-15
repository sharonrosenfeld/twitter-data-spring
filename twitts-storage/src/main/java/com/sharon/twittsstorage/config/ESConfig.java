package com.sharon.twittsstorage.config;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class ESConfig<esTemplate> {

    private final String index = "analyzed-twitts";

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(ESConfig.class);


    @Value(value = "${es.host}")
    private String esHost;

    @Value(value = "${es.port}")
    private int esPort;

    @Bean(destroyMethod = "close")
    public RestHighLevelClient restClient() {
        RestClientBuilder builder = RestClient.builder(
                new HttpHost(esHost, esPort));
        return new  RestHighLevelClient(builder);
    }

}
