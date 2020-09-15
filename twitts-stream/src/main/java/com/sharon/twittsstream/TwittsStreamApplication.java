package com.sharon.twittsstream;

import com.sharon.twittsstream.model.Twitt;
import com.sharon.twittsstream.service.PersistentQueueService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;


@SpringBootApplication
public class TwittsStreamApplication {

    public final static String[] INTUIT_PRODUCTS = {"QuickBooks", "TurnboTax", "Mint", "Intuit"};
    public final static String ENGLISH_LANG = "en";
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(TwittsStreamApplication.class);
    @Value(value = "${application.oauth.accesstoken}")
    String oauthAccessToken;

    @Value(value = "${application.oauth.accesstokensecret}")
    String oauthAccessTokenSecret;

    @Value(value = "${application.oauth.consumerkey}")
    String oauthConsumerKey;

    @Value(value = "${application.oauth.consumersecret}")
    String oauthConsumerSecret;

    @Autowired
    @Qualifier("KafkaQueueService")
    private PersistentQueueService queueService;

    public static void main(String[] args) {
        SpringApplication.run(TwittsStreamApplication.class, args);
    }

    @Bean
    TwitterStream twitterStream() throws TwitterException {
        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true).setOAuthConsumerKey(oauthConsumerKey)
                .setOAuthConsumerSecret(oauthConsumerSecret)
                .setOAuthAccessToken(oauthAccessToken)
                .setOAuthAccessTokenSecret(oauthAccessTokenSecret);
        TwitterStream twitterStream = new TwitterStreamFactory(cb.build())
                .getInstance();

        StatusListener listener = new StatusListener() {
            private String getProduct(String txt) {
                for (String product : INTUIT_PRODUCTS) {
                    if (StringUtils.containsIgnoreCase(txt, product)) {
                        return product;
                    }
                }
                return "";
            }

            @Override
            public void onStatus(Status status) {
                LOGGER.debug("new status: @" + status.getUser().getScreenName() + " - " + status.getText());
                queueService.sendMessage(new Twitt(status.getUser().getId(), status.getText(), status.getCreatedAt(), status.getFavoriteCount(), status.getId(), getProduct(status.getText())));
            }

            @Override
            public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {
                LOGGER.debug("Got a status deletion notice id:" + statusDeletionNotice.getStatusId());
                queueService.sendRemovedMessage(statusDeletionNotice.getStatusId());
            }

            @Override
            public void onTrackLimitationNotice(int numberOfLimitedStatuses) {
                LOGGER.debug("Got track limitation notice:" + numberOfLimitedStatuses);
            }

            @Override
            public void onScrubGeo(long userId, long upToStatusId) {
                LOGGER.debug("Got scrub_geo event userId:" + userId + " upToStatusId:" + upToStatusId);
            }

            @Override
            public void onStallWarning(StallWarning warning) {
                LOGGER.info("Got stall warning:" + warning);
            }

            @Override
            public void onException(Exception ex) {
                ex.printStackTrace();
            }
        };
        twitterStream.addListener(listener);
        FilterQuery filter = new FilterQuery();
        filter.track(TwittsStreamApplication.INTUIT_PRODUCTS);
        filter.language(TwittsStreamApplication.ENGLISH_LANG);
        twitterStream.filter(filter);
        return twitterStream;
    }
}
