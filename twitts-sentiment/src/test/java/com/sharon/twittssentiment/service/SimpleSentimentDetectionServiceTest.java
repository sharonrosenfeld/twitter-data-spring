package com.sharon.twittssentiment.service;

import com.sharon.twittssentiment.dto.Twitt;
import com.sharon.twittssentiment.model.Sentiment;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;

@RunWith(PowerMockRunner.class)
@SpringBootTest
public class SimpleSentimentDetectionServiceTest {

    @InjectMocks
    @Spy
    SentimentDetectionService service = new SimpleSentimentDetectionService();

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        Set<String> positive = new HashSet<>();
        positive.add("happy");
        Set<String> negative = new HashSet<>();
        negative.add("abuse");
        ReflectionTestUtils.setField(service, "positiveTerms",positive);
        ReflectionTestUtils.setField(service, "negativeTerms",negative);
    }

    @Test
    public void testDetect_positive() {
        Twitt twitt = new Twitt();
        twitt.setText("Im very happy to announce");
        Sentiment sentiment = service.detect(twitt);
        assertEquals(sentiment, Sentiment.POSITIVE);
    }

    @Test
    public void testDetect_negative() {
        Twitt twitt = new Twitt();
        twitt.setText("its certainly an abuse");
        Sentiment sentiment = service.detect(twitt);
        assertEquals(sentiment, Sentiment.NEGATIVE);
    }
}
