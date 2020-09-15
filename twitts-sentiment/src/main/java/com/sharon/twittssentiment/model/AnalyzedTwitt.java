package com.sharon.twittssentiment.model;

import lombok.Data;

import java.util.Date;

@Data
public class AnalyzedTwitt {
    Long userId;
    String text;
    Date createAt;
    Integer favouriteCount;
    Long tweetId;
    Sentiment sentiment;
    String product;

    public AnalyzedTwitt() {
    }

    public void setSentiment(Sentiment sentiment) {
        this.sentiment = sentiment;
    }
}
