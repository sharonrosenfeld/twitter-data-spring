package com.sharon.twittsstorage.dao.es;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Map;

import com.sharon.twittsstorage.model.AnalyzedTwitt;


@AllArgsConstructor
public class AnalyzedTwittsDao {
    private static final Logger LOGGER = LoggerFactory.getLogger(AnalyzedTwittsDao.class);

    private static final String ES_INDEX = "analyzed-twitts";

    private RestHighLevelClient esClient;

    private ObjectMapper objectMapper;

    public void delete(Long twittId) throws IOException {
        LOGGER.debug("deleting ANALYZED {}", twittId);
        DeleteRequest deleteRequest = new DeleteRequest(ES_INDEX)
                .id(twittId.toString());
        DeleteResponse deleteResponse = esClient.delete(deleteRequest, RequestOptions.DEFAULT);
        LOGGER.debug("es delete result {}",deleteResponse);
    }

    public void put(AnalyzedTwitt analyzedTwitt) throws IOException {
        LOGGER.debug("storing ANALYZED {}", analyzedTwitt);

        Map<String, Object> documentMapper = objectMapper.convertValue(analyzedTwitt, Map.class);

        IndexRequest indexRequest = new IndexRequest(ES_INDEX).id(analyzedTwitt.getTweetId().toString())
                .source(documentMapper);

        IndexResponse indexResponse = esClient.index(indexRequest,RequestOptions.DEFAULT);

        LOGGER.debug("es put index result {}",indexResponse);
    }

    public Map<String,Long> findProductSentimentsDateRange(String product, LocalDateTime from , LocalDateTime to) throws IOException{
        SearchRequest searchRequest = new SearchRequest(ES_INDEX);
        SearchSourceBuilder sourceBuilder = ProductSentimentsDateRangeQuery.buildQuery(from,to,product);
        searchRequest.source(sourceBuilder);
        SearchResponse searchResponse = esClient.search(searchRequest, RequestOptions.DEFAULT);
        return ProductSentimentsDateRangeQuery.parseResult(searchResponse);
    }



}
