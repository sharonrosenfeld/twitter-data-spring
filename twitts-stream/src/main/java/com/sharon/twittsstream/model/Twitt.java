package com.sharon.twittsstream.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class Twitt {
    Long userId;
    String text;
    Date createAt;
    Integer favouriteCount;
    Long tweetId;
    String product;

    public Twitt() {

    }

}
