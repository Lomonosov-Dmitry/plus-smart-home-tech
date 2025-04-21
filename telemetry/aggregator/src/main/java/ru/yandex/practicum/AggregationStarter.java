package ru.yandex.practicum;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.errors.WakeupException;
import org.apache.kafka.common.serialization.VoidDeserializer;
import org.apache.kafka.common.serialization.VoidSerializer;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.deserializer.SensorEventDeserializer;
import ru.yandex.practicum.kafka.serializer.GeneralKafkaSerializer;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorStateAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;

import java.time.Duration;
import java.util.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class AggregationStarter {
    private final KafkaConsumer<Void, SensorEventAvro> consumer;
    private final KafkaProducer<Void, SensorsSnapshotAvro> producer;
    private final Map<String, SensorsSnapshotAvro> snapshots;

    public AggregationStarter() {
        this.snapshots = new HashMap<>();
        Properties consProps = new Properties();
        consProps.put(ConsumerConfig.CLIENT_ID_CONFIG, "Consumer");
        consProps.put(ConsumerConfig.GROUP_ID_CONFIG, "group.id");
        consProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        consProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, VoidDeserializer.class.getCanonicalName());
        consProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, SensorEventDeserializer.class.getCanonicalName());
        this.consumer = new KafkaConsumer<>(consProps);
        Properties prodProps = new Properties();
        prodProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        prodProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, VoidSerializer.class);
        prodProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, GeneralKafkaSerializer.class);
        this.producer = new KafkaProducer<>(prodProps);

        Runtime.getRuntime().addShutdownHook(new Thread(consumer::wakeup));
    }

    public void start() {
        try {
            consumer.subscribe(List.of("telemetry.sensors.v1"));
            while (true) {

                handleRecords(consumer.poll(Duration.ofMillis(1000)));

                consumer.commitAsync();
            }
        } catch (WakeupException ignored) {

        } catch (Exception e) {
            log.error("Ошибка во время обработки событий от датчиков", e);
        } finally {
            consumer.commitSync();
            consumer.close();
            producer.flush();
            producer.close();
        }
    }

    private void handleRecords(ConsumerRecords<Void, SensorEventAvro> records) {
        List<SensorEventAvro> events = new ArrayList<>();
        for (ConsumerRecord<Void, SensorEventAvro> record : records) {
            events.add(record.value());
        }
        for (SensorEventAvro event : events) {
            log.info("Пришло событие датчика {} на хабе {}", event.getId(), event.getHubId());
            Optional<SensorsSnapshotAvro> snapshotAvro = updateState(event);
            if (snapshotAvro.isPresent()) {
                log.info("Пишем в очередь новый снапшот для хаба {}", snapshotAvro.get().getHubId());
                String topic = "telemetry.snapshots.v1";
                ProducerRecord<Void, SensorsSnapshotAvro> record = new ProducerRecord<>(topic, (SensorsSnapshotAvro) snapshotAvro.get());
                try {
                    producer.send(record);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    private Optional<SensorsSnapshotAvro> updateState(SensorEventAvro event) {
        SensorsSnapshotAvro snapshot = null;
        if (snapshots.containsKey(event.getHubId())) {
            SensorsSnapshotAvro oldState = snapshots.get(event.getHubId());
            if (oldState.getTimestamp().isAfter(event.getTimestamp()) || oldState.getTimestamp().equals(event.getTimestamp()))
                return Optional.empty();
            else
                snapshot = oldState;
        }
        if (snapshot == null) {
            log.info("Создаем новый снапшот для хаба {}", event.getHubId());
            snapshot = SensorsSnapshotAvro.newBuilder()
                    .setHubId(event.getHubId())
                    .setTimestamp(event.getTimestamp())
                    .setSensorsState(Map.of(event.getId(), SensorStateAvro.newBuilder()
                            .setTimestamp(event.getTimestamp())
                            .setData(event.getPayload())
                            .build()))
                    .build();
        } else {
            Map<String, SensorStateAvro> state = snapshot.getSensorsState();
            if (state.containsKey(event.getId())) {
                state.replace(event.getId(), SensorStateAvro.newBuilder()
                        .setTimestamp(event.getTimestamp())
                        .setData(event.getPayload())
                        .build());
                log.info("Заменяем состояние датчика {} у хаба {}", event.getId(), event.getHubId());
            } else {
                state.put(event.getId(), SensorStateAvro.newBuilder()
                        .setTimestamp(event.getTimestamp())
                        .setData(event.getPayload())
                        .build());
                log.info("Добавляем новый датчик {} у хаба {}", event.getId(), event.getHubId());
            }
        }
        log.info("Заменяем снапшот для хаба {}", event.getHubId());
        snapshots.replace(event.getHubId(), snapshot);
        return Optional.of(snapshot);
    }
}


