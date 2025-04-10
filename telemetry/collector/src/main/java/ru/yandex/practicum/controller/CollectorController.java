package ru.yandex.practicum.controller;

import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.model.hub_events.HubEvent;
import ru.yandex.practicum.model.hub_events.HubEventType;
import ru.yandex.practicum.model.sensor_events.SensorEvent;
import ru.yandex.practicum.model.sensor_events.SensorEventType;
import ru.yandex.practicum.service.handler.hub_handlers.HubEventHandler;
import ru.yandex.practicum.service.handler.sensor_handlers.SensorEventHandler;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;


@Validated
@RestController
@RequestMapping(value = "/events")
public class CollectorController {
    private final Map<SensorEventType, SensorEventHandler> sensorEventHandlers;
    private final Map<HubEventType, HubEventHandler> hubEventHandlers;

    public CollectorController(List<SensorEventHandler> sensorEventHandlers, List<HubEventHandler> hubEventHandlers) {
        this.sensorEventHandlers = sensorEventHandlers.stream()
                .collect(Collectors.toMap(SensorEventHandler::getMessageType, Function.identity()));
        this.hubEventHandlers = hubEventHandlers.stream()
                .collect(Collectors.toMap(HubEventHandler::getMessageType, Function.identity()));
    }

   @PostMapping("/sensors")
    public void getSensorEvent(@Valid @RequestBody SensorEvent event) {
        if (sensorEventHandlers.containsKey(event.getType()))
            sensorEventHandlers.get(event.getType()).handle(event);
        else
            throw new IllegalArgumentException("Нет обработчика для этого события");
    }

    @PostMapping("/hubs")
    public void getHubEvent(@Valid @RequestBody HubEvent event) {
        if (hubEventHandlers.containsKey(event.getType()))
            hubEventHandlers.get(event.getType()).handle(event);
        else
            throw new IllegalArgumentException("Нет обработчика для этого события");
    }

}
