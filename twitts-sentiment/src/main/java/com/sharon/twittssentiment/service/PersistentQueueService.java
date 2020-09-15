package com.sharon.twittssentiment.service;

import com.sharon.twittssentiment.model.AnalyzedTwitt;

public interface PersistentQueueService {
    void sendMessage(AnalyzedTwitt twitt);
}
