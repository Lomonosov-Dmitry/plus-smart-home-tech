package ru.yandex.practicum.service.handler.hub_handlers.json;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.DeviceAddedEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.DeviceTypeAvro;
import ru.yandex.practicum.model.hub_events.DeviceAddedEvent;
import ru.yandex.practicum.model.hub_events.HubEvent;
import ru.yandex.practicum.model.hub_events.HubEventType;
import ru.yandex.practicum.service.KafkaEventProducer;

@Component
public class DeviceAddedEventJsonHandler extends BaseHubEventJsonHandler<DeviceAddedEventAvro> implements HubEventJsonHandler {
    public DeviceAddedEventJsonHandler(KafkaEventProducer producer) {
        super(producer);
    }

    @Override
    protected DeviceAddedEventAvro mapToAvro(HubEvent event) {
        DeviceAddedEvent dvEvent = (DeviceAddedEvent) event;
        return DeviceAddedEventAvro.newBuilder()
                .setId(dvEvent.getId())
                .setType(DeviceTypeAvro.valueOf(dvEvent.getDeviceType().toString()))
                .build();
    }

    @Override
    public HubEventType getMessageType() {
        return HubEventType.DEVICE_ADDED;
    }
}
