package com.sharon.twittsstream.service;

import com.sharon.twittsstream.model.Twitt;

public interface PersistentQueueService {
    void sendMessage(Twitt twitt);

    void sendRemovedMessage(Long twittId);
}
