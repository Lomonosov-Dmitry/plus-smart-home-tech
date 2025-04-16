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
import ru.yandex.practicum.service.handler.hub_handlers.json.HubEventJsonHandler;
import ru.yandex.practicum.service.handler.sensor_handlers.json.SensorEventJsonHandler;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;


@Validated
@RestController
@RequestMapping(value = "/events")
public class CollectorController {
    private final Map<SensorEventType, SensorEventJsonHandler> sensorEventHandlers;
    private final Map<HubEventType, HubEventJsonHandler> hubEventHandlers;

    public CollectorController(List<SensorEventJsonHandler> sensorEventJsonHandlers, List<HubEventJsonHandler> hubEventJsonHandlers) {
        this.sensorEventHandlers = sensorEventJsonHandlers.stream()
                .collect(Collectors.toMap(SensorEventJsonHandler::getMessageType, Function.identity()));
        this.hubEventHandlers = hubEventJsonHandlers.stream()
                .collect(Collectors.toMap(HubEventJsonHandler::getMessageType, Function.identity()));
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
