package com.sharon.twittssentiment.service;

import com.sharon.twittssentiment.model.AnalyzedTwitt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaQueueService implements PersistentQueueService {

    private final String ANALYZED_TWITTS_TOPIC = "analyzed-twitts";

    @Autowired
    private KafkaTemplate<String, AnalyzedTwitt> kafkaTemplate;

    @Override
    public void sendMessage(AnalyzedTwitt twitt) {
        kafkaTemplate.send(ANALYZED_TWITTS_TOPIC, twitt);
    }

}
