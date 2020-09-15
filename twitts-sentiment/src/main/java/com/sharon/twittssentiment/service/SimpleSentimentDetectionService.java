package com.sharon.twittssentiment.service;

import com.sharon.twittssentiment.dto.Twitt;
import com.sharon.twittssentiment.model.Sentiment;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Service
public class SimpleSentimentDetectionService implements SentimentDetectionService {

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(SimpleSentimentDetectionService.class);

    private static final String TWITT_DELIMITER = " ";

    @Autowired
    @Qualifier("positiveSentiments")
    private Set<String> positiveTerms;

    @Autowired
    @Qualifier("negativeSentiments")
    private Set<String> negativeTerms;

    @Override
    public Sentiment detect(Twitt twitt) {
        String[] textarr = twitt.getText().split(TWITT_DELIMITER);
        Set<String> text = new HashSet<>();
        Collections.addAll(text, textarr);

        if (!Collections.disjoint(text, positiveTerms)) {
            return Sentiment.POSITIVE;
        }
        if (!Collections.disjoint(text, negativeTerms)) {
            return Sentiment.NEGATIVE;
        }
        return Sentiment.NEUTRAL;
    }

}
