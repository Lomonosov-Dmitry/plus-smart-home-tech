package ru.yandex.practicum.configuration;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.VoidSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import ru.yandex.practicum.kafka.serializer.GeneralKafkaSerializer;

import java.util.Properties;

@org.springframework.context.annotation.Configuration
@ConfigurationProperties("collector.kafka")
public class Configuration {
    @Value("${kafka.bootstrap_server}")
    private String bootstrapSever;

    @Bean
    Properties producerProperties() {
        Properties prodProps = new Properties();
        prodProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapSever);
        prodProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, VoidSerializer.class);
        prodProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, GeneralKafkaSerializer.class);
        return prodProps;
    }
}
