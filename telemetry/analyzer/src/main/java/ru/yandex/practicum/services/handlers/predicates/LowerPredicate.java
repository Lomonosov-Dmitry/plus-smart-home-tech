package ru.yandex.practicum.services.handlers.predicates;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.ConditionOperationAvro;

@Component
public class LowerPredicate implements PredicateHandler {
    @Override
    public ConditionOperationAvro getType() {
        return ConditionOperationAvro.LOWER_THAN;
    }

    @Override
    public Boolean compare(Integer a, Integer b) {
        return a < b;
    }
}
