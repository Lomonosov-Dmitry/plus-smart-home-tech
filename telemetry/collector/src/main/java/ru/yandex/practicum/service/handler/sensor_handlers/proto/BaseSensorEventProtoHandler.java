package ru.yandex.practicum.service.handler.sensor_handlers.proto;

import lombok.RequiredArgsConstructor;
import org.apache.avro.specific.SpecificRecordBase;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;
import ru.yandex.practicum.model.EventType;
import ru.yandex.practicum.service.KafkaEventProducer;

import java.time.Instant;


@RequiredArgsConstructor
public abstract class BaseSensorEventProtoHandler<T extends SpecificRecordBase> implements SensorEventProtoHandler {
    protected final KafkaEventProducer producer;

    protected abstract T mapToAvro(SensorEventProto event);

    @Override
    public void handle(SensorEventProto event) {
        if (!event.getPayloadCase().equals(getMessageType())) {
            throw new IllegalArgumentException("Неизвестный тип сообщения");
        }
        T payload = mapToAvro(event);

        SensorEventAvro sensorEventAvro = SensorEventAvro.newBuilder()
                .setHubId(event.getHubId())
                .setId(event.getId())
                .setTimestamp(Instant.ofEpochSecond(event.getTimestamp().getSeconds(), event.getTimestamp().getNanos()))
                .setPayload(payload)
                .build();

        producer.send(sensorEventAvro, EventType.SENSOR_EVENT);
    }
}
