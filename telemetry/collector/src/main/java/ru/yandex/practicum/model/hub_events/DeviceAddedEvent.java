package ru.yandex.practicum.model.hub_events;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class DeviceAddedEvent extends HubEvent {
    DeviceType deviceType;
    String id;

    @Override
    public HubEventType getType() {
        return HubEventType.DEVICE_ADDED;
    }
}
