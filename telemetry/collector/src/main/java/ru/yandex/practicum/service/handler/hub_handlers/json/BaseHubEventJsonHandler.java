package ru.yandex.practicum.service.handler.hub_handlers.json;

import lombok.RequiredArgsConstructor;
import org.apache.avro.specific.SpecificRecordBase;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.model.EventType;
import ru.yandex.practicum.model.hub_events.HubEvent;
import ru.yandex.practicum.service.KafkaEventProducer;

@RequiredArgsConstructor
public abstract class BaseHubEventJsonHandler<T extends SpecificRecordBase> implements HubEventJsonHandler {
    protected final KafkaEventProducer producer;

    protected abstract T mapToAvro(HubEvent event);

    @Override
    public void handle(HubEvent event) {
        if (!event.getType().equals(getMessageType())) {
            throw new IllegalArgumentException("Неизвестный тип сообщения");
        }
        T payload = mapToAvro(event);

        HubEventAvro hubEventAvro = HubEventAvro.newBuilder()
                .setHubId(event.getHubId())
                .setTimestamp(event.getTimestamp())
                .setPayload(payload)
                .build();

        producer.send(hubEventAvro, EventType.HUB_EVENT);
    }
}
