package com.sharon.twittssentiment;

import com.sharon.twittssentiment.dto.Twitt;
import com.sharon.twittssentiment.service.SentimentDetectionService;
import com.sharon.twittssentiment.service.SentimentService;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.KafkaListener;

@SpringBootApplication
public class TwittsSentimentApplication {

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(TwittsSentimentApplication.class);

    private final String TWEET_TOPIC = "twitts";

    @Autowired
    private SentimentService service;

    public static void main(String[] args) {
        SpringApplication.run(TwittsSentimentApplication.class, args);
    }

    @KafkaListener(topics = TWEET_TOPIC)
    public void twittListener(Twitt twitt) {
        LOGGER.debug("received from kafka: {} ", twitt);
        try {
            service.handle(twitt);
        } catch (Exception e) {
            LOGGER.error("exception caught while handling twitt {} ", e.getMessage());
        }
    }
}
