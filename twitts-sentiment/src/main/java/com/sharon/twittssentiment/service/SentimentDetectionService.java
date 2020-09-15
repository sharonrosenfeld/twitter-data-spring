package com.sharon.twittssentiment.service;

import com.sharon.twittssentiment.dto.Twitt;
import com.sharon.twittssentiment.model.Sentiment;

public interface SentimentDetectionService {
    Sentiment detect(Twitt twitt);
}
