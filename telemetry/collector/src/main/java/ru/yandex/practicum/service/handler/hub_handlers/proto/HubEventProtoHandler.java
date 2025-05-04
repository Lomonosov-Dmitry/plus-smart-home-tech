package ru.yandex.practicum.service.handler.hub_handlers.proto;

import ru.yandex.practicum.grpc.telemetry.event.HubEventProto;

public interface HubEventProtoHandler {
    HubEventProto.PayloadCase getMessageType();

    void handle(HubEventProto event);
}
