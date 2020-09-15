package com.sharon.twittsstorage.controller;

import com.sharon.twittsstorage.dao.es.AnalyzedTwittsDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping(value = "/twitts")
public class TwittsStorageController {

    private static final Logger LOGGER = LoggerFactory.getLogger(TwittsStorageController.class);

    @Autowired
    AnalyzedTwittsDao analyzedTwittsDao;

    @GetMapping(value = "/sentiment")
    public Map<String, Long> getSentiment(@RequestParam(value = "product") String product, @RequestParam(value = "from") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) String from, @RequestParam(value = "to") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) String to) {
        LocalDateTime dateFrom = LocalDateTime.parse(from);
        LocalDateTime dateTo = LocalDateTime.parse(to);
        try {
            return analyzedTwittsDao.findProductSentimentsDateRange(product, dateFrom, dateTo);
        } catch (IOException e) {
            LOGGER.error("exception occured ", e.getMessage());
            return Collections.emptyMap();
        }
    }
}

