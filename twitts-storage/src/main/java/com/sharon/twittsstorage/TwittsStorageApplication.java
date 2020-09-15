package com.sharon.twittsstorage;


import com.sharon.twittsstorage.dao.es.AnalyzedTwittsDao;
import com.sharon.twittsstorage.model.AnalyzedTwitt;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.KafkaListener;

@SpringBootApplication
public class TwittsStorageApplication {

    private static final String ANALYZED_TWITTS_TOPIC = "analyzed-twitts";
    private static final String REMOVED_TWITTS_TOPIC = "removed-twitts";

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(TwittsStorageApplication.class);

    @Autowired
    private AnalyzedTwittsDao dao;

    public static void main(String[] args) {
        SpringApplication.run(TwittsStorageApplication.class, args);
    }

    @KafkaListener(topics = ANALYZED_TWITTS_TOPIC)
    public void twittListener(AnalyzedTwitt twitt) {
        LOGGER.debug("received from kafka: {} ", twitt);
        try {
            dao.put(twitt);
        } catch (Exception e) {
            LOGGER.error("exception caught while handling twitt {} ", e.getMessage());
        }
    }

    @KafkaListener(topics = REMOVED_TWITTS_TOPIC)
    public void removedTwittListener(Long twittId) {
        LOGGER.debug("received from kafka: {} ", twittId);
        try {
            dao.delete(twittId);
        } catch (Exception e) {
            LOGGER.error("exception caught while handling twitt {} ", e.getMessage());
        }
    }

}

