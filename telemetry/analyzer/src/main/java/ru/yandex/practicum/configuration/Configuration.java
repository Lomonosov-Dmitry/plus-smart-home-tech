package ru.yandex.practicum.configuration;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.VoidDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import ru.yandex.practicum.kafka.deserializer.HubEventDeserializer;
import ru.yandex.practicum.kafka.deserializer.SensorSnapshotDeserializer;

import java.util.Properties;

@org.springframework.context.annotation.Configuration
public class Configuration {
    @Value("${kafka.bootstrap_server}")
    private String kafkaBootstrap_server;

    @Bean
    Properties consumerHubProperties() {
        Properties consProps = new Properties();
        consProps.put(ConsumerConfig.CLIENT_ID_CONFIG, "HubConsumer");
        consProps.put(ConsumerConfig.GROUP_ID_CONFIG, "group.id");
        consProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaBootstrap_server);
        consProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, VoidDeserializer.class.getCanonicalName());
        consProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, HubEventDeserializer.class.getCanonicalName());
        return consProps;
    }

    @Bean
    Properties consumerSnapshotProperties() {
        Properties consProps = new Properties();
        consProps.put(ConsumerConfig.CLIENT_ID_CONFIG, "SnapshotConsumer");
        consProps.put(ConsumerConfig.GROUP_ID_CONFIG, "group1.id");
        consProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaBootstrap_server);
        consProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, VoidDeserializer.class.getCanonicalName());
        consProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, SensorSnapshotDeserializer.class.getCanonicalName());
        return consProps;
    }
}