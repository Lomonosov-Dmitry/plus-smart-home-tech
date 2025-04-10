package ru.yandex.practicum.model.hub_events;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ScenarioCondition {
    String sensorId;
    ScenarioConditionType type;
    ScenarioOperation operation;
    Integer value;
}
