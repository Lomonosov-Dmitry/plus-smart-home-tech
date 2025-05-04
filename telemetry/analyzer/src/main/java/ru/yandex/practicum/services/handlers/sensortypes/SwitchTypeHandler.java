package ru.yandex.practicum.services.handlers.sensortypes;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.ConditionTypeAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorStateAvro;
import ru.yandex.practicum.kafka.telemetry.event.SwitchSensorAvro;

@Component
public class SwitchTypeHandler implements SensorTypeHandler {
    @Override
    public ConditionTypeAvro getType() {
        return ConditionTypeAvro.SWITCH;
    }

    @Override
    public Integer getValue(SensorStateAvro sensor) {
        SwitchSensorAvro swSensor = (SwitchSensorAvro) sensor.getData();
        if (swSensor.getState())
            return 1;
        else
            return 0;
    }
}
