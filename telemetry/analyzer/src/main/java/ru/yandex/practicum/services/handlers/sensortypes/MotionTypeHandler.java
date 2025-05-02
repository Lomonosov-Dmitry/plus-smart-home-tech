package ru.yandex.practicum.services.handlers.sensortypes;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.ConditionTypeAvro;
import ru.yandex.practicum.kafka.telemetry.event.MotionSensorAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorStateAvro;

@Component
public class MotionTypeHandler implements SensorTypeHandler {

    @Override
    public ConditionTypeAvro getType() {
        return ConditionTypeAvro.MOTION;
    }

    @Override
    public Integer getValue(SensorStateAvro sensor) {

        MotionSensorAvro mSensor = (MotionSensorAvro) sensor.getData();

        if (mSensor.getMotion())
            return 1;
        else
            return 0;
    }
}
