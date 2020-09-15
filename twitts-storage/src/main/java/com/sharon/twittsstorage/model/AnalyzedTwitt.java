package com.sharon.twittsstorage.model;

import lombok.Data;

import java.util.Date;

@Data
public class AnalyzedTwitt {
    Long userId;
    String text;
    Date createAt;
    Integer favouriteCount;
    Long tweetId;
    String sentiment;
    String product;

    public AnalyzedTwitt() {
    }
}
