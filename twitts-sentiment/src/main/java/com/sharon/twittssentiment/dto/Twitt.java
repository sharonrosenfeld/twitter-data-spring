package com.sharon.twittssentiment.dto;

import lombok.Data;

import java.util.Date;


@Data
public class Twitt {
    String product;
    private Long userId;
    private String text;
    private Date createAt;
    private Integer favouriteCount;
    private Long tweetId;
}
