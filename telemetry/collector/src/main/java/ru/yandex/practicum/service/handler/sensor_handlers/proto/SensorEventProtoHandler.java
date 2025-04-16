package ru.yandex.practicum.service.handler.sensor_handlers.proto;

import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;

public interface SensorEventProtoHandler {
    SensorEventProto.PayloadCase getMessageType();

    void handle(SensorEventProto event);
}
