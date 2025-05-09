package ru.yandex.practicum.service.handler.hub_handlers.json;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.DeviceRemovedEventAvro;
import ru.yandex.practicum.model.hub_events.DeviceRemovedEvent;
import ru.yandex.practicum.model.hub_events.HubEvent;
import ru.yandex.practicum.model.hub_events.HubEventType;
import ru.yandex.practicum.service.KafkaEventProducer;

@Component
public class DeviceRemovedEventJsonHandler extends BaseHubEventJsonHandler<DeviceRemovedEventAvro> implements HubEventJsonHandler {
    public DeviceRemovedEventJsonHandler(KafkaEventProducer producer) {
        super(producer);
    }

    @Override
    protected DeviceRemovedEventAvro mapToAvro(HubEvent event) {
        DeviceRemovedEvent drEvent = (DeviceRemovedEvent) event;
        return DeviceRemovedEventAvro.newBuilder()
                .setId(drEvent.getId())
                .build();
    }

    @Override
    public HubEventType getMessageType() {
        return HubEventType.DEVICE_REMOVED;
    }
}
