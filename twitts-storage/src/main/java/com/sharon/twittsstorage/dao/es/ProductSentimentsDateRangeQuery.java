package com.sharon.twittsstorage.dao.es;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedStringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

class ProductSentimentsDateRangeQuery {

    private static final String CREATE_DATE_FIELD = "createAt";
    private static final String PRODUCT_FIELD = "product";
    private static final String SENTIMENT_FIELD = "sentiment";
    private static final String SENTIMENT_AGG = "sentiment";

    static SearchSourceBuilder buildQuery(LocalDateTime from, LocalDateTime to, String product) {
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();

        QueryBuilder baseSearchQuery = getDateProductQueryBuilder(from, to, product);
        sourceBuilder.query(baseSearchQuery);
        AggregationBuilder aggregation = AggregationBuilders
                .terms(SENTIMENT_AGG)
                .field(SENTIMENT_FIELD);
        sourceBuilder.aggregation(aggregation);

        return sourceBuilder;
    }

    private static QueryBuilder getDateProductQueryBuilder(LocalDateTime from, LocalDateTime to, String product) {

        QueryBuilder rangeQuery = QueryBuilders
                .rangeQuery(CREATE_DATE_FIELD)
                .gte(from)
                .lt(to);

        QueryBuilder productQuery = QueryBuilders
                .matchQuery(PRODUCT_FIELD, product);

        return QueryBuilders
                .boolQuery()
                .must(rangeQuery).must(productQuery);
    }

    static Map<String, Long> parseResult(SearchResponse sr) {
        Map<String, Long> counts = new HashMap<>();
        ParsedStringTerms sentimentsAndCounts = sr.getAggregations().get(SENTIMENT_AGG);

        for (Terms.Bucket bucket : sentimentsAndCounts.getBuckets()) {
            String key = bucket.getKey().toString();
            long docCount = bucket.getDocCount();            // Doc count
            counts.put(key, docCount);
        }
        return counts;
    }

}
