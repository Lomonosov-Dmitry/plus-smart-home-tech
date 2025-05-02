package ru.yandex.practicum.services;

import com.google.protobuf.Timestamp;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.grpc.telemetry.event.ActionTypeProto;
import ru.yandex.practicum.grpc.telemetry.event.DeviceActionProto;
import ru.yandex.practicum.grpc.telemetry.event.DeviceActionRequest;
import ru.yandex.practicum.grpc.telemetry.hubrouter.HubRouterControllerGrpc;
import ru.yandex.practicum.kafka.telemetry.event.*;
import ru.yandex.practicum.model.Condition;
import ru.yandex.practicum.model.Scenario;
import ru.yandex.practicum.services.handlers.predicates.PredicateHandler;
import ru.yandex.practicum.services.handlers.sensortypes.SensorTypeHandler;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
public class SnapshotService {

    private final HubService hubService;
    private final Map<String, SensorsSnapshotAvro> hubState = new HashMap<>();
    private final Map<ConditionOperationAvro, PredicateHandler> predicates;
    private final Map<ConditionTypeAvro, SensorTypeHandler> sensorTypes;
    private final HubRouterControllerGrpc.HubRouterControllerBlockingStub hubRouterClient;

    public SnapshotService(List<PredicateHandler> predicateHandlers,
                           List<SensorTypeHandler> sensorTypeHandlers,
                           HubService hubService,
                           @GrpcClient("hub-router")
                           HubRouterControllerGrpc.HubRouterControllerBlockingStub hubRouterClient) {
        this.predicates = predicateHandlers.stream()
                .collect(Collectors.toMap(PredicateHandler::getType, Function.identity()));
        this.sensorTypes = sensorTypeHandlers.stream()
                .collect(Collectors.toMap(SensorTypeHandler::getType, Function.identity()));
        this.hubService = hubService;
        this.hubRouterClient = hubRouterClient;
    }

    public void handleSnapshot(SensorsSnapshotAvro snapshot) {
        String hubId = snapshot.getHubId();
        if (!hubState.containsKey(hubId)) {
            hubState.put(hubId, snapshot);
        } else {
            if (snapshot.getTimestamp().isAfter(hubState.get(hubId).getTimestamp()) || snapshot.getTimestamp().equals(hubState.get(hubId).getTimestamp())) {
                hubState.replace(hubId, snapshot);
            }
        }
        checkScenarios(snapshot);
    }

    public void checkScenarios(SensorsSnapshotAvro snapshot) {
        List<Scenario> scenarios = hubService.getScenarios().stream()
                .filter(scenario -> scenario.getHubId().equals(snapshot.getHubId()))
                .toList();
        for (Scenario scenario : scenarios) {
            boolean acted = false;
            for (String sensorName : scenario.getConditions().keySet()) {
                if (snapshot.getSensorsState().containsKey(sensorName)) {
                    Condition condition = scenario.getConditions().get(sensorName);
                    SensorStateAvro sensorState = snapshot.getSensorsState().get(sensorName);
                    int sensorValue = sensorTypes.get(condition.getType()).getValue(sensorState);
                    if (!predicates.get(condition.getOperation()).compare(sensorValue, condition.getValue())) {
                        acted = false;
                        break;
                    } else
                        acted = true;
                }
            }
            if (acted) {
                log.info("Работаем по сценарию {}", scenario.getName());
                for (String s : scenario.getActions().keySet()) {
                    log.info("Выполняем действие по {}", s);
                    ActionTypeProto typeProto = mapToProto(scenario.getActions().get(s).getType());
                    DeviceActionProto.Builder actionProto = DeviceActionProto.newBuilder();
                    actionProto.setSensorId(s);
                    actionProto.setType(typeProto);
                    actionProto.setValue(scenario.getActions().get(s).getValue());
                    actionProto.build();

                    DeviceActionRequest request = DeviceActionRequest.newBuilder()
                            .setHubId(scenario.getHubId())
                            .setScenarioName(scenario.getName())
                            .setTimestamp(Timestamp.newBuilder()
                                    .setSeconds(Instant.now().getEpochSecond())
                                    .setNanos(Instant.now().getNano())
                                    .build())
                            .setAction(actionProto)
                            .build();
                    log.info("Создали запрос, отправляем");
                    hubRouterClient.handleDeviceAction(request);
                }
            }
        }
    }

    private ActionTypeProto mapToProto(ActionTypeAvro type) {
        switch (type) {
            case INVERSE -> {
                return ActionTypeProto.INVERSE;
            }
            case ACTIVATE -> {
                return ActionTypeProto.ACTIVATE;
            }
            case SET_VALUE -> {
                return ActionTypeProto.SET_VALUE;
            }
            case DEACTIVATE -> {
                return ActionTypeProto.DEACTIVATE;
            }
            default -> {
                return ActionTypeProto.UNRECOGNIZED;
            }
        }
    }
}


