package com.sharon.twittssentiment.config;

import com.sharon.twittssentiment.service.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;


@Configuration
public class ApplicationConfig {

    @Value(value = "${application.executor.minthreads:1}")
    Integer minthreadsexecutor;
    @Value(value = "${application.executor.maxthreads:4}")
    Integer maxthreadsexecutor;
    @Value(value = "${application.executor.idletime:100}")
    Integer idleTimeexecutor;
    @Value(value = "${application.executor.queuesz:10}")
    Integer queueusz;
    @Value(value = "${application.positive_file:/positive.txt}")
    private String positiveFile;
    @Value(value = "${application.negative_file:/negative.txt}")
    private String negativeFile;

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean("SimpleSentimentDetectionService")
    public SentimentDetectionService sentimentDetectionService() {
        return new SimpleSentimentDetectionService();
    }

    @Bean
    public SentimentService sentimentService() {
        return new SentimentService();
    }

    @Bean("positiveSentiments")
    public Set<String> positiveSentiments(){
        return createWordsSet(positiveFile);
    }

    @Bean("negativeSentiments")
    public Set<String> negativeSentiments(){
        return createWordsSet(negativeFile);
    }

    private Set<String> createWordsSet(String file) {
        Set<String> terms = new HashSet<>();
        Resource resource = new ClassPathResource(file);
        try (InputStream inputStream = resource.getInputStream();
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            terms = reader.lines().filter(s -> !s.startsWith(";")).filter(s -> !s.isEmpty()).collect(Collectors.toSet());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return terms;
    }

    @Bean
    public Executor sentimentExecutor() {
        return new ThreadPoolExecutor(minthreadsexecutor, maxthreadsexecutor, idleTimeexecutor, TimeUnit.MILLISECONDS, new ArrayBlockingQueue(queueusz));
    }

    @Bean("KafkaQueueService")
    public PersistentQueueService persistentQueueService() {
        return new KafkaQueueService();
    }

}
