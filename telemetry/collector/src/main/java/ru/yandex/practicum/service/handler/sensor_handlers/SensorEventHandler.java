package ru.yandex.practicum.service.handler.sensor_handlers;

import ru.yandex.practicum.model.sensor_events.SensorEvent;
import ru.yandex.practicum.model.sensor_events.SensorEventType;

public interface SensorEventHandler {
    SensorEventType getMessageType();

    void handle(SensorEvent event);
}
