package ru.yandex.practicum.service.handler.sensor_handlers.json;

import ru.yandex.practicum.model.sensor_events.SensorEvent;
import ru.yandex.practicum.model.sensor_events.SensorEventType;

public interface SensorEventJsonHandler {
    SensorEventType getMessageType();

    void handle(SensorEvent event);
}
