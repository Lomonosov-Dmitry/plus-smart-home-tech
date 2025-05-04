package ru.yandex.practicum.service.handler.hub_handlers.json;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.*;
import ru.yandex.practicum.model.hub_events.*;
import ru.yandex.practicum.service.KafkaEventProducer;

import java.util.ArrayList;
import java.util.List;

@Component
public class ScenarioAddedEventJsonHandler extends BaseHubEventJsonHandler<ScenarioAddedEventAvro> implements HubEventJsonHandler {
    public ScenarioAddedEventJsonHandler(KafkaEventProducer producer) {
        super(producer);
    }

    @Override
    protected ScenarioAddedEventAvro mapToAvro(HubEvent event) {
        ScenarioAddedEvent scEvent = (ScenarioAddedEvent) event;
        List<ScenarioConditionAvro> conditions = new ArrayList<>();
        for (ScenarioCondition condition : scEvent.getConditions()) {
            conditions.add(ScenarioConditionAvro.newBuilder()
                    .setOperation(ConditionOperationAvro.valueOf(condition.getOperation().toString()))
                    .setSensorId(condition.getSensorId())
                    .setType(ConditionTypeAvro.valueOf(condition.getType().toString()))
                    .setValue(condition.getValue())
                    .build());
        }
        List<DeviceActionAvro> actions = new ArrayList<>();
        for (DeviceAction action : scEvent.getActions()) {
            actions.add(DeviceActionAvro.newBuilder()
                    .setType(ActionTypeAvro.valueOf(action.getType().toString()))
                    .setSensorId(action.getSensorId())
                    .setValue(action.getValue())
                    .build());
        }
        return ScenarioAddedEventAvro.newBuilder()
                .setConditions(conditions)
                .setActions(actions)
                .setName(scEvent.getName())
                .build();
    }

    @Override
    public HubEventType getMessageType() {
        return HubEventType.SCENARIO_ADDED;
    }
}
