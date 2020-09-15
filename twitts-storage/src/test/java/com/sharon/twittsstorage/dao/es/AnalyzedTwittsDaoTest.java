package com.sharon.twittsstorage.dao.es;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.lucene.search.TotalHits;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedStringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest({SearchSourceBuilder.class, Aggregations.class, SearchHits.class, TotalHits.class})
public class AnalyzedTwittsDaoTest {

    @Mock
    ObjectMapper mapper;

    private Aggregations aggregations;
    private SearchResponse searchResponse;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        searchResponse = PowerMockito.mock(SearchResponse.class);
        aggregations = PowerMockito.mock(Aggregations.class);
        PowerMockito.when(searchResponse.getAggregations()).thenReturn(aggregations);
        List<Aggregation> aggregationsList = new ArrayList<>();
        Aggregation agg = PowerMockito.mock(Aggregation.class);
        aggregationsList.add(agg);
        ReflectionTestUtils.setField(aggregations, "aggregations", aggregationsList);
    }

    @Test
    public void testParseResult_WithResultsSuccess() {
        ParsedStringTerms sentimentsAndCounts = PowerMockito.mock(ParsedStringTerms.class);
        PowerMockito.when(aggregations.get(anyString())).thenReturn(sentimentsAndCounts);

        List<Terms.Bucket> buckets = new ArrayList<>();
        Terms.Bucket bucket1 = mock(Terms.Bucket.class);
        buckets.add(bucket1);
        Terms.Bucket bucket2 = mock(Terms.Bucket.class);
        buckets.add(bucket2);
        doReturn(buckets).when(sentimentsAndCounts).getBuckets();
        when(bucket1.getKey()).thenReturn("positive");
        when(bucket1.getDocCount()).thenReturn(Long.valueOf(3));
        when(bucket2.getKey()).thenReturn("neutral");
        when(bucket2.getDocCount()).thenReturn(Long.valueOf(10));

        Map<String, Long> ret = ProductSentimentsDateRangeQuery.parseResult(searchResponse);

        assertEquals(Long.valueOf(3), ret.get("positive"));
        assertEquals(Long.valueOf(10), ret.get("neutral"));
        assertEquals(2, ret.size());
        verify(aggregations, times(1)).get(any());
    }

    @Test
    public void testParseResult_NoResultsSuccess() {
        List<Terms.Bucket> buckets = new ArrayList<>();
        ParsedStringTerms sentimentsAndCounts = PowerMockito.mock(ParsedStringTerms.class);
        doReturn(buckets).when(sentimentsAndCounts).getBuckets();
        PowerMockito.when(aggregations.get(anyString())).thenReturn(sentimentsAndCounts);
        Map<String, Long> ret = ProductSentimentsDateRangeQuery.parseResult(searchResponse);
        assertEquals(0, ret.size());
    }
}
