package ru.yandex.practicum.service.handler.hub_handlers.json;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.ScenarioRemovedEventAvro;
import ru.yandex.practicum.model.hub_events.HubEvent;
import ru.yandex.practicum.model.hub_events.HubEventType;
import ru.yandex.practicum.model.hub_events.ScenarioRemovedEvent;
import ru.yandex.practicum.service.KafkaEventProducer;

@Component
public class ScenarioRemovedEventJsonHandler extends BaseHubEventJsonHandler<ScenarioRemovedEventAvro> implements HubEventJsonHandler {
    public ScenarioRemovedEventJsonHandler(KafkaEventProducer producer) {
        super(producer);
    }

    @Override
    protected ScenarioRemovedEventAvro mapToAvro(HubEvent event) {
        ScenarioRemovedEvent scEvent = (ScenarioRemovedEvent) event;
        return ScenarioRemovedEventAvro.newBuilder()
                .setName(scEvent.getName())
                .build();
    }

    @Override
    public HubEventType getMessageType() {
        return HubEventType.SCENARIO_REMOVED;
    }
}
