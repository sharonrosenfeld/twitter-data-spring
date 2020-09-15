package com.sharon.twittsstream.service;

import com.sharon.twittsstream.model.Twitt;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaQueueService implements PersistentQueueService {

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(KafkaQueueService.class);
    private final String TWITTS_TOPIC = "twitts";
    private final String REMOVED_TWITTS_TOPIC = "removed-twitts";
    @Autowired
    private KafkaTemplate<String, Twitt> twittKafkaTemplate;

    @Autowired
    @Qualifier("deletedTwittKafkaTemplate")
    private KafkaTemplate<String, Long> deletedTwittKafkaTemplate;

    @Override
    public void sendMessage(Twitt twitt) {
        LOGGER.debug("sending {}", twitt);
        twittKafkaTemplate.send(TWITTS_TOPIC, twitt);
    }

    @Override
    public void sendRemovedMessage(Long twittId) {
        deletedTwittKafkaTemplate.send(REMOVED_TWITTS_TOPIC, twittId);
    }

}
