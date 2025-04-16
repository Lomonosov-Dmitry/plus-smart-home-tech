package ru.yandex.practicum.controller;

import com.google.protobuf.Empty;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import ru.yandex.practicum.grpc.telemetry.collector.CollectorControllerGrpc;
import ru.yandex.practicum.grpc.telemetry.event.HubEventProto;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;
import ru.yandex.practicum.service.handler.hub_handlers.proto.HubEventProtoHandler;
import ru.yandex.practicum.service.handler.sensor_handlers.proto.SensorEventProtoHandler;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@GrpcService
public class EventController extends CollectorControllerGrpc.CollectorControllerImplBase {
    private final Map<SensorEventProto.PayloadCase, SensorEventProtoHandler> sensorEventHandlers;
    private final Map<HubEventProto.PayloadCase, HubEventProtoHandler> hubEventHandlers;

    public EventController(List<SensorEventProtoHandler> sensorEventProtoHandlers, List<HubEventProtoHandler> hubEventProtoHandlers) {
        this.sensorEventHandlers = sensorEventProtoHandlers.stream()
                .collect(Collectors.toMap(SensorEventProtoHandler::getMessageType, Function.identity()));
        this.hubEventHandlers = hubEventProtoHandlers.stream()
                .collect(Collectors.toMap(HubEventProtoHandler::getMessageType, Function.identity()));
    }

    @Override
    public void collectSensorEvent(SensorEventProto event, StreamObserver<Empty> responseObserver) {
        if (sensorEventHandlers.containsKey(event.getPayloadCase()))
            sensorEventHandlers.get(event.getPayloadCase()).handle(event);
        else
            throw new IllegalArgumentException("Нет обработчика для этого события");
    }

    @Override
    public void collectHubEvent(HubEventProto event, StreamObserver<Empty> responseObserver) {
        if (hubEventHandlers.containsKey(event.getPayloadCase()))
            hubEventHandlers.get(event.getPayloadCase()).handle(event);
        else
            throw new IllegalArgumentException("Нет обработчика для этого события");
    }
}
