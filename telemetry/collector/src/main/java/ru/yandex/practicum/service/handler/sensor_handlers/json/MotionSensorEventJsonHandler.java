package ru.yandex.practicum.service.handler.sensor_handlers.json;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.MotionSensorAvro;
import ru.yandex.practicum.model.sensor_events.MotionSensorEvent;
import ru.yandex.practicum.model.sensor_events.SensorEvent;
import ru.yandex.practicum.model.sensor_events.SensorEventType;
import ru.yandex.practicum.service.KafkaEventProducer;

@Component
public class MotionSensorEventJsonHandler extends BaseSensorEventJsonHandler<MotionSensorAvro> implements SensorEventJsonHandler {
    public MotionSensorEventJsonHandler(KafkaEventProducer producer) {
        super(producer);
    }

    @Override
    protected MotionSensorAvro mapToAvro(SensorEvent event) {
        MotionSensorEvent moEvent = (MotionSensorEvent) event;
        return MotionSensorAvro.newBuilder()
                .setMotion(moEvent.isMotion())
                .setLinkQuality(moEvent.getLinkQuality())
                .setVoltage(moEvent.getVoltage())
                .build();
    }

    @Override
    public SensorEventType getMessageType() {
        return SensorEventType.MOTION_SENSOR_EVENT;
    }
}
