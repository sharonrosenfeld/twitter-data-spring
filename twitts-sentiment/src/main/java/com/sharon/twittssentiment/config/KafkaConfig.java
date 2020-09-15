package com.sharon.twittssentiment.config;

import com.sharon.twittssentiment.dto.Twitt;
import com.sharon.twittssentiment.model.AnalyzedTwitt;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

@EnableKafka
@Configuration
public class KafkaConfig {

    private final String groupId = "twitts_sentiment";
    @Value(value = "${kafka.bootstrapAddress}")
    private String bootstrapAddress;

    @Bean
    public ConsumerFactory<String, Twitt> consumerFactory() {
        Map<String, Object> props = new HashMap();
        props.put(
                ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,
                bootstrapAddress);
        props.put(
                ConsumerConfig.GROUP_ID_CONFIG,
                groupId);
        props.put(
                ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
                StringDeserializer.class);
        props.put(
                ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
                JsonDeserializer.class);

        return new DefaultKafkaConsumerFactory<>(props, null, new JsonDeserializer(Twitt.class));
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, Twitt>
    kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, Twitt> factory =
                new ConcurrentKafkaListenerContainerFactory();
        factory.setConsumerFactory(consumerFactory());
        return factory;
    }

    ///////////////////////////////// producer //////////////////////////////////////////

    @Bean
    public ProducerFactory<String, AnalyzedTwitt> producerFactory() {
        Map<String, Object> configProps = new HashMap();
        configProps.put(
                ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,
                bootstrapAddress);
        configProps.put(
                ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
                StringSerializer.class);
        configProps.put(
                ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
                JsonSerializer.class);
        return new DefaultKafkaProducerFactory(configProps);
    }

    @Bean
    public KafkaTemplate<String, AnalyzedTwitt> kafkaTemplate() {
        return new KafkaTemplate(producerFactory());
    }
}