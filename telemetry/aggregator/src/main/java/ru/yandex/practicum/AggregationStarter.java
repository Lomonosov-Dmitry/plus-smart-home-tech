package ru.yandex.practicum;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.errors.WakeupException;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorStateAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;
import org.springframework.beans.factory.annotation.Value;

import java.time.Duration;
import java.util.*;

@Slf4j
@Component
public class AggregationStarter {

    private final KafkaConsumer<Void, SensorEventAvro> consumer;
    private final KafkaProducer<Void, SensorsSnapshotAvro> producer;
    private final Map<String, SensorsSnapshotAvro> snapshots;
    @Value("${kafka.sensor_topic}")
    private String sensorTopic;
    @Value("${kafka.snapshot_topic}")
    private String snapshotTopic;

    public AggregationStarter(Properties consumerProperties, Properties producerProperties) {
        this.snapshots = new HashMap<>();
        this.consumer = new KafkaConsumer<>(consumerProperties);
        this.producer = new KafkaProducer<>(producerProperties);

        Runtime.getRuntime().addShutdownHook(new Thread(consumer::wakeup));
    }

    public void start() {
        try {
            consumer.subscribe(List.of(sensorTopic));
            while (true) {

                handleRecords(consumer.poll(Duration.ofMillis(1000)));

                consumer.commitSync();
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
                String topic = snapshotTopic;
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
        if (snapshots.containsKey(event.getHubId())) {
            log.info("Нашли снапшот для хаба {}", event.getHubId());
            SensorsSnapshotAvro snapshot = snapshots.get(event.getHubId());
            if (snapshot.getSensorsState().containsKey(event.getId())) {
                log.info("Нашли в снапшоте для хаба {} состояние датчика {}", event.getHubId(), event.getId());
                SensorStateAvro oldState = snapshot.getSensorsState().get(event.getId());
                if (oldState.getTimestamp().isAfter(event.getTimestamp())) {
                    log.info("Хранящийся тайстамп для датчика {} новее пришедшего", event.getId());
                    return Optional.empty();
                } else {
                    if (oldState.getData().equals(event.getPayload()))
                    {
                        log.info("Состояние датчика {} не изменилось, ничего не пишем", event.getId());
                        return Optional.empty();
                    } else {
                        Map<String, SensorStateAvro> states = new HashMap<>(snapshot.getSensorsState());
                        log.info("Было {}", snapshot.getSensorsState().get(event.getId()).getData().toString());
                        SensorStateAvro newState = SensorStateAvro.newBuilder()
                                .setTimestamp(event.getTimestamp())
                                .setData(event.getPayload())
                                .build();
                        states.replace(event.getId(), newState);
                        snapshot.setSensorsState(states);
                        log.info("Заменили состояние датчика {} в хабе {}", event.getId(), event.getHubId());
                        log.info("Пришло {}", event.getPayload().toString());
                        log.info("Стало {}", snapshot.getSensorsState().get(event.getId()).getData().toString());
                        snapshots.replace(event.getHubId(), snapshot);
                        log.info("Заменили таймстамп для хаба {}", event.getHubId());
                        return Optional.of(snapshot);
                    }
                }
            } else {
                log.info("Пришел новый датчик {} для хаба {}", event.getId(), event.getHubId());
                Map<String, SensorStateAvro> states = new HashMap<>(snapshot.getSensorsState());
                SensorStateAvro newState = SensorStateAvro.newBuilder()
                        .setTimestamp(event.getTimestamp())
                        .setData(event.getPayload())
                        .build();
                states.put(event.getId(), newState);
                snapshot.setSensorsState(states);
                log.info("Записали состояние датчика {} в хабе {}", event.getId(), event.getHubId());
                log.info("Пришло {}", event.getPayload().toString());
                log.info("Стало {}", snapshot.getSensorsState().get(event.getId()).getData().toString());
                snapshots.replace(event.getHubId(), snapshot);
                log.info("Заменили таймстамп для хаба {}", event.getHubId());
                return Optional.of(snapshot);
            }
        } else {
            SensorsSnapshotAvro snapshot = SensorsSnapshotAvro.newBuilder()
                    .setHubId(event.getHubId())
                    .setTimestamp(event.getTimestamp())
                    .setSensorsState(Map.of(event.getId(), SensorStateAvro.newBuilder()
                            .setTimestamp(event.getTimestamp())
                            .setData(event.getPayload())
                            .build()))
                    .build();
            snapshots.put(event.getHubId(), snapshot);
            log.info("Создали новый таймстамп для хаба {}", event.getHubId());
            return Optional.of(snapshot);
        }
    }
}


