package ru.yandex.practicum.services.handlers.sensortypes;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.ClimateSensorAvro;
import ru.yandex.practicum.kafka.telemetry.event.ConditionTypeAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorStateAvro;

@Component
public class Co2levelTypeHandler implements SensorTypeHandler {
    @Override
    public ConditionTypeAvro getType() {
        return ConditionTypeAvro.CO2LEVEL;
    }

    @Override
    public Integer getValue(SensorStateAvro sensor) {
        ClimateSensorAvro cSensor = (ClimateSensorAvro) sensor.getData();
        return cSensor.getCo2Level();
    }
}
