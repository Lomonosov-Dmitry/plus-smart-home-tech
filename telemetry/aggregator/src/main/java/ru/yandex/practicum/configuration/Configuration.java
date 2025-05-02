package ru.yandex.practicum.configuration;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.VoidDeserializer;
import org.apache.kafka.common.serialization.VoidSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import ru.yandex.practicum.kafka.deserializer.SensorEventDeserializer;
import ru.yandex.practicum.kafka.serializer.GeneralKafkaSerializer;

import java.util.Properties;

@org.springframework.context.annotation.Configuration
public class Configuration {
    @Value("${kafka.bootstrap_server}")
    private String kafkaBootstrap_server;

    @Bean
    Properties consumerProperties() {
        Properties consProps = new Properties();
        consProps.put(ConsumerConfig.CLIENT_ID_CONFIG, "Consumer");
        consProps.put(ConsumerConfig.GROUP_ID_CONFIG, "group.id");
        consProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaBootstrap_server);
        consProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, VoidDeserializer.class.getCanonicalName());
        consProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, SensorEventDeserializer.class.getCanonicalName());
        return consProps;
    }

    @Bean
    Properties producerProperties() {
        Properties prodProps = new Properties();
        prodProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaBootstrap_server);
        prodProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, VoidSerializer.class);
        prodProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, GeneralKafkaSerializer.class);
        return prodProps;
    }
}
