package ru.yandex.practicum.service.handler.hub_handlers.proto;

import lombok.RequiredArgsConstructor;
import org.apache.avro.specific.SpecificRecordBase;
import ru.yandex.practicum.grpc.telemetry.event.HubEventProto;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.model.EventType;
import ru.yandex.practicum.service.KafkaEventProducer;

import java.time.Instant;


@RequiredArgsConstructor
public abstract class BaseHubEventProtoHandler<T extends SpecificRecordBase> implements HubEventProtoHandler {
    protected final KafkaEventProducer producer;

    protected abstract T mapToAvro(HubEventProto event);

    @Override
    public void handle(HubEventProto event) {
        if (!event.getPayloadCase().equals(getMessageType())) {
            throw new IllegalArgumentException("Неизвестный тип сообщения");
        }
        T payload = mapToAvro(event);

        HubEventAvro hubEventAvro = HubEventAvro.newBuilder()
                .setHubId(event.getHubId())
                .setTimestamp(Instant.ofEpochSecond(event.getTimestamp().getSeconds(), event.getTimestamp().getNanos()))
                .setPayload(payload)
                .build();

        producer.send(hubEventAvro, EventType.HUB_EVENT);
    }
}
