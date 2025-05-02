package ru.yandex.practicum.processors;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.errors.WakeupException;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;
import ru.yandex.practicum.services.SnapshotService;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

@Slf4j
@Component
public class SnapshotProcessor {
    private final KafkaConsumer<Void, SensorsSnapshotAvro> consumer;
    private final SnapshotService snapshotService;

    public SnapshotProcessor(Properties consumerSnapshotProperties, SnapshotService snapshotService) {
        this.consumer = new KafkaConsumer<>(consumerSnapshotProperties);
        this.snapshotService = snapshotService;
    }

    public void start() {
        try {
            consumer.subscribe(List.of("telemetry.snapshots.v1"));
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
        }
    }

    public void handleRecords(ConsumerRecords<Void, SensorsSnapshotAvro> snapshotsAvro) {
        List<SensorsSnapshotAvro> snapshots = new ArrayList<>();
        for (ConsumerRecord<Void, SensorsSnapshotAvro> record : snapshotsAvro) {
            snapshots.add(record.value());
        }
        for (SensorsSnapshotAvro snapshot : snapshots) {
            snapshotService.handleSnapshot(snapshot);
        }
    }
}
