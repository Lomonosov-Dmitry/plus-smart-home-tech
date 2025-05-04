package ru.yandex.practicum.services.handlers.predicates;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.ConditionOperationAvro;

@Component
public class EqualsPredicate implements PredicateHandler {
    @Override
    public ConditionOperationAvro getType() {
        return ConditionOperationAvro.EQUALS;
    }

    @Override
    public Boolean compare(Integer a, Integer b) {
        return a.equals(b);
    }
}
