package ru.yandex.practicum.services.handlers.predicates;

import ru.yandex.practicum.kafka.telemetry.event.ConditionOperationAvro;

public interface PredicateHandler {
    ConditionOperationAvro getType();

    Boolean compare(Integer a, Integer b);
}
