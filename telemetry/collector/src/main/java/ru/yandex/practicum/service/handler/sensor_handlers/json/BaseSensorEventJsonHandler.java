package ru.yandex.practicum.service.handler.sensor_handlers.json;

import lombok.RequiredArgsConstructor;
import org.apache.avro.specific.SpecificRecordBase;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;
import ru.yandex.practicum.model.EventType;
import ru.yandex.practicum.model.sensor_events.SensorEvent;
import ru.yandex.practicum.service.KafkaEventProducer;

@RequiredArgsConstructor
public abstract class BaseSensorEventJsonHandler<T extends SpecificRecordBase> implements SensorEventJsonHandler {
    protected final KafkaEventProducer producer;

    protected abstract T mapToAvro(SensorEvent event);

    @Override
    public void handle(SensorEvent event) {
        if (!event.getType().equals(getMessageType())) {
            throw new IllegalArgumentException("Неизвестный тип сообщения");
        }
        T payload = mapToAvro(event);

        SensorEventAvro sensorEventAvro = SensorEventAvro.newBuilder()
                .setHubId(event.getHubId())
                .setId(event.getId())
                .setTimestamp(event.getTimestamp())
                .setPayload(payload)
                .build();

        producer.send(sensorEventAvro, EventType.SENSOR_EVENT);
    }
}
