package ru.yandex.practicum.services.handlers.sensortypes;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.ClimateSensorAvro;
import ru.yandex.practicum.kafka.telemetry.event.ConditionTypeAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorStateAvro;
import ru.yandex.practicum.kafka.telemetry.event.TemperatureSensorAvro;

@Component
public class TemperatureTypeHandler implements SensorTypeHandler {
    @Override
    public ConditionTypeAvro getType() {
        return ConditionTypeAvro.TEMPERATURE;
    }

    @Override
    public Integer getValue(SensorStateAvro sensor) {
        return switch (sensor.getData()) {
            case TemperatureSensorAvro tt -> tt.getTemperatureC();
            case ClimateSensorAvro cs -> cs.getTemperatureC();
            default -> null;
        };
    }
}
