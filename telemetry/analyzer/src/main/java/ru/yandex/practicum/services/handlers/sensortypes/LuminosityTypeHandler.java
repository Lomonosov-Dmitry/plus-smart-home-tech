package ru.yandex.practicum.services.handlers.sensortypes;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.ConditionTypeAvro;
import ru.yandex.practicum.kafka.telemetry.event.LightSensorAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorStateAvro;

@Component
public class LuminosityTypeHandler implements SensorTypeHandler {
    @Override
    public ConditionTypeAvro getType() {
        return ConditionTypeAvro.LUMINOSITY;
    }

    @Override
    public Integer getValue(SensorStateAvro sensor) {
        LightSensorAvro lSensor = (LightSensorAvro) sensor.getData();
        return lSensor.getLuminosity();
    }
}
