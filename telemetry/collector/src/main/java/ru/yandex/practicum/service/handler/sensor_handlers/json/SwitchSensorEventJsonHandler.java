package ru.yandex.practicum.service.handler.sensor_handlers.json;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.SwitchSensorAvro;
import ru.yandex.practicum.model.sensor_events.SensorEvent;
import ru.yandex.practicum.model.sensor_events.SensorEventType;
import ru.yandex.practicum.model.sensor_events.SwitchSensorEvent;
import ru.yandex.practicum.service.KafkaEventProducer;

@Component
public class SwitchSensorEventJsonHandler extends BaseSensorEventJsonHandler<SwitchSensorAvro> implements SensorEventJsonHandler {
    public SwitchSensorEventJsonHandler(KafkaEventProducer producer) {
        super(producer);
    }

    @Override
    protected SwitchSensorAvro mapToAvro(SensorEvent event) {
        SwitchSensorEvent swEvent = (SwitchSensorEvent) event;
        return SwitchSensorAvro.newBuilder()
                .setState(swEvent.isState())
                .build();
    }

    @Override
    public SensorEventType getMessageType() {
        return SensorEventType.SWITCH_SENSOR_EVENT;
    }
}
