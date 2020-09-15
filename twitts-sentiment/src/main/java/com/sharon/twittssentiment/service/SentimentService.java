package com.sharon.twittssentiment.service;

import com.sharon.twittssentiment.dto.Twitt;
import com.sharon.twittssentiment.model.AnalyzedTwitt;
import com.sharon.twittssentiment.model.Sentiment;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.concurrent.Executor;

@Service
public class SentimentService {

    @Autowired
    @Qualifier("SimpleSentimentDetectionService")
    SentimentDetectionService detectionService;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    Executor sentimentDetectionWorkers;
    @Autowired
    @Qualifier("KafkaQueueService")
    PersistentQueueService persistentQueueService;

    public void handle(Twitt twitt) {
        sentimentDetectionWorkers.execute(new Runnable() {
            @Override
            public void run() {
                Sentiment sentiment = detectionService.detect(twitt);
                AnalyzedTwitt analyzedTwitt = convertTweet(twitt, sentiment);
                persistentQueueService.sendMessage(analyzedTwitt);
            }
        });
    }

    private AnalyzedTwitt convertTweet(Twitt twitt, Sentiment sentiment) {
        AnalyzedTwitt analyzedTwitt1 = modelMapper.map(twitt, AnalyzedTwitt.class);
        analyzedTwitt1.setSentiment(sentiment);
        return analyzedTwitt1;
    }
}
