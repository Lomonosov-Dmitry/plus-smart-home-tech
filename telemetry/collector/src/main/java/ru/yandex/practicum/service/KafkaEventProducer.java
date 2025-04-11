package ru.yandex.practicum.service;

import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.VoidSerializer;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.serializer.GeneralKafkaSerializer;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;
import ru.yandex.practicum.model.EventType;

import java.util.Properties;

@Component
public class KafkaEventProducer {
    Properties config = new Properties();

    public KafkaEventProducer() {
        this.config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        this.config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, VoidSerializer.class);
        this.config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, GeneralKafkaSerializer.class);
    }

    public <T extends SpecificRecordBase> void send(T event, EventType type) {
        switch (type) {
            case HUB_EVENT -> {
                String topic = "telemetry.hubs.v1";
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
                String topic = "telemetry.sensors.v1";
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
