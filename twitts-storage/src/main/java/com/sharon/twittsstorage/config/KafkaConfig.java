package com.sharon.twittsstorage.config;

import com.sharon.twittsstorage.model.AnalyzedTwitt;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.LongDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

@EnableKafka
@Configuration
public class KafkaConfig {

    private final String twittsGroupId = "analyzed-twitts";
    private final String removedTwittsGroupId = "removed-twitts";
    @Value(value = "${kafka.bootstrapAddress}")
    private String bootstrapAddress;

    @Bean
    public ConsumerFactory<String, AnalyzedTwitt> consumerFactory() {
        Map<String, Object> props = new HashMap();
        props.put(
                ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,
                bootstrapAddress);
        props.put(
                ConsumerConfig.GROUP_ID_CONFIG,
                twittsGroupId);
        props.put(
                ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
                StringDeserializer.class);
        props.put(
                ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
                JsonDeserializer.class);

        return new DefaultKafkaConsumerFactory<>(props, null, new JsonDeserializer(AnalyzedTwitt.class));
    }

    @Bean("deletedTwittConsumerFactory")
    public ConsumerFactory<String, Long> deletedTwittConsumerFactory() {
        Map<String, Object> props = new HashMap();
        props.put(
                ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,
                bootstrapAddress);
        props.put(
                ConsumerConfig.GROUP_ID_CONFIG,
                removedTwittsGroupId);
        props.put(
                ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
                StringDeserializer.class);
        props.put(
                ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
                LongDeserializer.class);

        return new DefaultKafkaConsumerFactory<>(props, null, null);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, AnalyzedTwitt>
    kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, AnalyzedTwitt> factory =
                new ConcurrentKafkaListenerContainerFactory();
        factory.setConsumerFactory(consumerFactory());
        return factory;
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, Long>
    kafkaListenerDeletedTwittContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, Long> factory =
                new ConcurrentKafkaListenerContainerFactory();
        factory.setConsumerFactory(deletedTwittConsumerFactory());
        return factory;
    }
}