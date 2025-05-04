package ru.yandex.practicum.processors;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.errors.WakeupException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.*;
import ru.yandex.practicum.services.HubService;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

@Slf4j
@Component
public class HubEventProcessor implements Runnable {
    private final KafkaConsumer<Void, HubEventAvro> consumer;
    private final HubService hubService;
    @Value("${kafka.hub_topic}")
    private String hubTopic;

    public HubEventProcessor(Properties consumerHubProperties, HubService hubService) {
        this.consumer = new KafkaConsumer<>(consumerHubProperties);
        this.hubService = hubService;
        Thread release = new Thread(consumer::wakeup);
        Runtime.getRuntime().addShutdownHook(release);
    }

    @Override
    public void run() {
        try {
            consumer.subscribe(List.of(hubTopic));
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

    public void handleRecords(ConsumerRecords<Void, HubEventAvro> records) {
        List<HubEventAvro> events = new ArrayList<>();
        for (ConsumerRecord<Void, HubEventAvro> record : records) {
            events.add(record.value());
        }
        for (HubEventAvro event : events) {
            String hubId = event.getHubId();
            switch (event.getPayload()) {
                case DeviceAddedEventAvro d: {
                    log.info("Пришло событие добавления устройства {} для хаба {}", d.getId(), hubId);
                    hubService.addDevice(d, hubId);
                    break;
                }
                case DeviceRemovedEventAvro r: {
                    log.info("Пришло событие на удаление устройства {}", r.getId());
                    hubService.removeDevice(r, hubId);
                    break;
                }
                case ScenarioAddedEventAvro sa: {
                    log.info("Пришло событие на добавление нового сценария на хаб {}", hubId);
                    hubService.addScenario(sa, hubId);
                    break;
                }
                case ScenarioRemovedEventAvro sr: {
                    log.info("Удаляем сценарий {} на хабе {}", sr.getName(), hubId);
                    hubService.removeScenario(sr, hubId);
                    break;
                }
                default:
                    throw new IllegalStateException("Unexpected value: " + event.getPayload());
            }
        }
    }
}


