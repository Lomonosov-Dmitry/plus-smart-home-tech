package ru.yandex.practicum.service.handler.sensor_handlers.json;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.TemperatureSensorAvro;
import ru.yandex.practicum.model.sensor_events.SensorEvent;
import ru.yandex.practicum.model.sensor_events.SensorEventType;
import ru.yandex.practicum.model.sensor_events.TemperatureSensorEvent;
import ru.yandex.practicum.service.KafkaEventProducer;

@Component
public class TemperatureSensorEventJsonHandler extends BaseSensorEventJsonHandler<TemperatureSensorAvro> implements SensorEventJsonHandler {
    public TemperatureSensorEventJsonHandler(KafkaEventProducer producer) {
        super(producer);
    }

    @Override
    protected TemperatureSensorAvro mapToAvro(SensorEvent event) {
        TemperatureSensorEvent tEvent = (TemperatureSensorEvent) event;
        return TemperatureSensorAvro.newBuilder()
                .setTemperatureC(tEvent.getTemperature_c())
                .setTemperatureF(tEvent.getTemperature_f())
                .build();
    }

    @Override
    public SensorEventType getMessageType() {
        return SensorEventType.TEMPERATURE_SENSOR_EVENT;
    }
}
