package ru.yandex.practicum.service.handler.hub_handlers.json;

import ru.yandex.practicum.model.hub_events.HubEvent;
import ru.yandex.practicum.model.hub_events.HubEventType;

public interface HubEventJsonHandler {
    HubEventType getMessageType();

    void handle(HubEvent event);
}
