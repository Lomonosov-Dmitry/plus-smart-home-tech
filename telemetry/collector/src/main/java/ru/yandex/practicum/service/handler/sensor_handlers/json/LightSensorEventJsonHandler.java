package ru.yandex.practicum.service.handler.sensor_handlers.json;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.LightSensorAvro;
import ru.yandex.practicum.model.sensor_events.LightSensorEvent;
import ru.yandex.practicum.model.sensor_events.SensorEvent;
import ru.yandex.practicum.model.sensor_events.SensorEventType;
import ru.yandex.practicum.service.KafkaEventProducer;

@Component
public class LightSensorEventJsonHandler extends BaseSensorEventJsonHandler<LightSensorAvro> implements SensorEventJsonHandler {
    public LightSensorEventJsonHandler(KafkaEventProducer producer) {
        super(producer);
    }

    @Override
    protected LightSensorAvro mapToAvro(SensorEvent event) {
        LightSensorEvent liEvent = (LightSensorEvent) event;
        return LightSensorAvro.newBuilder()
                .setLinkQuality(liEvent.getLinkQuality())
                .setLuminosity(liEvent.getLuminosity())
                .build();
    }

    @Override
    public SensorEventType getMessageType() {
        return SensorEventType.LIGHT_SENSOR_EVENT;
    }
}
