package ru.yandex.practicum.service.handler.sensor_handlers.json;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.ClimateSensorAvro;
import ru.yandex.practicum.model.sensor_events.ClimateSensorEvent;
import ru.yandex.practicum.model.sensor_events.SensorEvent;
import ru.yandex.practicum.model.sensor_events.SensorEventType;
import ru.yandex.practicum.service.KafkaEventProducer;

@Component
public class ClimateSensorEventJsonHandler extends BaseSensorEventJsonHandler<ClimateSensorAvro> implements SensorEventJsonHandler {
    public ClimateSensorEventJsonHandler(KafkaEventProducer producer) {
        super(producer);
    }

    @Override
    protected ClimateSensorAvro mapToAvro(SensorEvent event) {
        ClimateSensorEvent cliEvent = (ClimateSensorEvent) event;
        return ClimateSensorAvro.newBuilder()
                .setCo2Level(cliEvent.getCo2Level())
                .setHumidity(cliEvent.getHumidity())
                .setTemperatureC(cliEvent.getTemperatureC())
                .build();
    }

    @Override
    public SensorEventType getMessageType() {
        return SensorEventType.CLIMATE_SENSOR_EVENT;
    }
}
