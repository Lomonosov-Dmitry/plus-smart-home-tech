package ru.yandex.practicum.services.handlers.sensortypes;

import ru.yandex.practicum.kafka.telemetry.event.ConditionTypeAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorStateAvro;

public interface SensorTypeHandler {
    ConditionTypeAvro getType();

    Integer getValue(SensorStateAvro sensor);
}
