package ru.yandex.practicum.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.VoidSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.serializer.GeneralKafkaSerializer;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;
import ru.yandex.practicum.model.EventType;

import java.util.Properties;

@Slf4j
@Component
public class KafkaEventProducer {
    @Value("${kafka.hub_topic}")
    private String hubTopic;
    @Value("${kafka.sensor_topic}")
    private String sensorTopic;
    Properties config;

    public KafkaEventProducer(Properties producerProperties) {
        this.config = producerProperties;
    }

    public <T extends SpecificRecordBase> void send(T event, EventType type) {
        switch (type) {
            case HUB_EVENT -> {
                log.info("Пишем событие в очередь хабов");
                String topic = hubTopic;
                ProducerRecord<String, HubEventAvro> record = new ProducerRecord<>(topic, (HubEventAvro) event);
                try(Producer<String, HubEventAvro> producer = new KafkaProducer<>(config)) {
                    producer.send(record);
                    producer.flush();
                    producer.close();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
            case SENSOR_EVENT -> {
                log.info("Пишем событие в очередь сенсоров");
                String topic = sensorTopic;
                ProducerRecord<String, SensorEventAvro> record = new ProducerRecord<>(topic, (SensorEventAvro) event);
                try (Producer<String, SensorEventAvro> producer = new KafkaProducer<>(config)) {
                    producer.send(record);
                    producer.flush();
                    producer.close();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
