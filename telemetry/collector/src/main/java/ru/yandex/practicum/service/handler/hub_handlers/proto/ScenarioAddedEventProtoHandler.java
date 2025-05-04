package ru.yandex.practicum.service.handler.hub_handlers.proto;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.grpc.telemetry.event.DeviceActionProto;
import ru.yandex.practicum.grpc.telemetry.event.HubEventProto;
import ru.yandex.practicum.grpc.telemetry.event.ScenarioAddedEventProto;
import ru.yandex.practicum.grpc.telemetry.event.ScenarioConditionProto;
import ru.yandex.practicum.kafka.telemetry.event.*;
import ru.yandex.practicum.service.KafkaEventProducer;

import java.util.ArrayList;
import java.util.List;

@Component
public class ScenarioAddedEventProtoHandler extends BaseHubEventProtoHandler<ScenarioAddedEventAvro> implements HubEventProtoHandler {
    public ScenarioAddedEventProtoHandler(KafkaEventProducer producer) {
        super(producer);
    }

    @Override
    protected ScenarioAddedEventAvro mapToAvro(HubEventProto event) {
        ScenarioAddedEventProto proto = event.getScenarioAdded();
        List<ScenarioConditionAvro> conditions = new ArrayList<>();
        for (ScenarioConditionProto condition : proto.getConditionList()) {
            ScenarioConditionAvro.Builder builder = ScenarioConditionAvro.newBuilder();
            ScenarioConditionProto.ValueCase valueCase = condition.getValueCase();
            switch (valueCase) {
                case INT_VALUE ->  builder.setValue(condition.getIntValue());
                case BOOL_VALUE -> builder.setValue(condition.getBoolValue());
            }
            builder.setOperation(ConditionOperationAvro.valueOf(condition.getOperation().toString()));
            builder.setSensorId(condition.getSensorId());
            builder.setType(ConditionTypeAvro.valueOf(condition.getType().toString()));
            conditions.add(builder.build());
        }
        List<DeviceActionAvro> actions = new ArrayList<>();
        for (DeviceActionProto action : proto.getActionList()) {
            actions.add(DeviceActionAvro.newBuilder()
                    .setType(ActionTypeAvro.valueOf(action.getType().toString()))
                    .setSensorId(action.getSensorId())
                    .setValue(action.getValue())
                    .build());
        }
        return ScenarioAddedEventAvro.newBuilder()
                .setConditions(conditions)
                .setActions(actions)
                .setName(proto.getName())
                .build();
    }

    @Override
    public HubEventProto.PayloadCase getMessageType() {
        return HubEventProto.PayloadCase.SCENARIO_ADDED;
    }
}
