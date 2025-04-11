package ru.yandex.practicum.service.handler.hub_handlers;

import ru.yandex.practicum.model.hub_events.HubEvent;
import ru.yandex.practicum.model.hub_events.HubEventType;

public interface HubEventHandler {
    HubEventType getMessageType();

    void handle(HubEvent event);
}
