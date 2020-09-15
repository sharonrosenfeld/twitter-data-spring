package com.sharon.twittsstream.config;

import com.sharon.twittsstream.model.Twitt;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.LongSerializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConfig {

    @Value(value = "${kafka.bootstrapAddress}")
    private String bootstrapAddress;

    @Bean
    public ProducerFactory<String, Twitt> producerFactory() {
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

    @Bean("deletedTwittProducerFactory")
    public ProducerFactory<String, Long> deletedTwittProducerFactory() {
        Map<String, Object> configProps = new HashMap();
        configProps.put(
                ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,
                bootstrapAddress);
        configProps.put(
                ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
                StringSerializer.class);
        configProps.put(
                ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
                LongSerializer.class);
        return new DefaultKafkaProducerFactory(configProps);
    }

    @Bean
    public KafkaTemplate<String, Twitt> kafkaTemplate() {
        return new KafkaTemplate(producerFactory());
    }

    @Bean("deletedTwittKafkaTemplate")
    public KafkaTemplate<String, Long> deletedTwittsKafkaTemplate() {
        return new KafkaTemplate(deletedTwittProducerFactory());
    }

}
