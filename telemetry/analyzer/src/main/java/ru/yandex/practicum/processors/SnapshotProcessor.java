package ru.yandex.practicum.processors;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.errors.WakeupException;
import org.springframework.beans.factory.annotation.Value;
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
    @Value("${kafka.snapshot_topic}")
    private String snapshotTopic;

    public SnapshotProcessor(Properties consumerSnapshotProperties, SnapshotService snapshotService) {
        this.consumer = new KafkaConsumer<>(consumerSnapshotProperties);
        this.snapshotService = snapshotService;
        Thread release = new Thread(consumer::wakeup);
        Runtime.getRuntime().addShutdownHook(release);
    }

    public void start() {
        try {
            consumer.subscribe(List.of(snapshotTopic));
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
