package ru.yandex.practicum.services;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.kafka.telemetry.event.*;
import ru.yandex.practicum.model.Action;
import ru.yandex.practicum.model.Condition;
import ru.yandex.practicum.model.Scenario;
import ru.yandex.practicum.model.Sensor;
import ru.yandex.practicum.repositories.ActionRepository;
import ru.yandex.practicum.repositories.ConditionRepository;
import ru.yandex.practicum.repositories.ScenarioRepository;
import ru.yandex.practicum.repositories.SensorRepository;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class HubService {
    private final ScenarioRepository scenarioRepository;
    private final ActionRepository actionRepository;
    private final ConditionRepository conditionRepository;
    private final SensorRepository sensorRepository;
    @Getter
    private final List<Scenario> scenarios;

    public HubService(ScenarioRepository scenarioRepository,
                      ActionRepository actionRepository,
                      ConditionRepository conditionRepository,
                      SensorRepository sensorRepository) {
        this.scenarioRepository = scenarioRepository;
        this.actionRepository = actionRepository;
        this.conditionRepository = conditionRepository;
        this.sensorRepository = sensorRepository;
        this.scenarios = scenarioRepository.findAll();;
    }

    public void removeDevice(DeviceRemovedEventAvro event, String hubId) {
        if (!event.getId().isEmpty()) {
            List<Scenario> inConditions = getScenariosBySensorIdInConditions(event.getId(), hubId);
            if (!inConditions.isEmpty()) {
                for (Scenario scenario : inConditions) {
                    scenario.getConditions().remove(event.getId());
                    scenarioRepository.save(scenario);
                }
            }
            List<Scenario> inActions = getScenariosBySensorIdInActions(event.getId(), hubId);
            if (!inActions.isEmpty()) {
                for (Scenario scenario : inActions) {
                    scenario.getActions().remove(event.getId());
                    scenarioRepository.save(scenario);
                }
            }
            sensorRepository.deleteById(event.getId());
        }
        log.info("Удалили устройство {}", event.getId());
    }

    public void addDevice(DeviceAddedEventAvro event, String hubId) {
        if (sensorRepository.findByIdAndHubId(event.getId(), hubId).isEmpty()) {
            Sensor newSensor = new Sensor();
            newSensor.setId(event.getId());
            newSensor.setHubId(hubId);
            sensorRepository.save(newSensor);
            log.info("Добавили устройство {} на хабе {}", event.getId(), hubId);
        }
    }

    public void addScenario(ScenarioAddedEventAvro event, String hubId) {
        if (scenarioRepository.findByHubIdAndName(hubId, event.getName()).isEmpty()) {
            Scenario scenario = new Scenario();
            scenario.setName(event.getName());
            scenario.setHubId(hubId);
            for (ScenarioConditionAvro condition : event.getConditions()) {
                scenario.getConditions().put(condition.getSensorId(), addCondition(condition));
            }
            for (DeviceActionAvro action : event.getActions()) {
                scenario.getActions().put(action.getSensorId(), addAction(action));
            }
            scenarioRepository.save(scenario);
            scenarios.add(scenario);
            log.info("Добавили сценарий {} на хабе {}", scenario.getName(), hubId);
        }
    }

    public void removeScenario(ScenarioRemovedEventAvro event, String hubId) {
        Optional<Scenario> scenarioOptional = scenarioRepository.findByHubIdAndName(hubId, event.getName());
        if (scenarioOptional.isPresent()) {
            scenarioRepository.delete(scenarioOptional.get());
            scenarios.remove(scenarioOptional.get());
        }
        log.info("Удалили сценарий {} на хабе {}", event.getName(), hubId);
    }

    private Condition addCondition(ScenarioConditionAvro conditionAvro) {
        Condition condition = new Condition();
        condition.setType(conditionAvro.getType());
        condition.setOperation(conditionAvro.getOperation());
        switch (conditionAvro.getValue()) {
            case Boolean b: {
                if (b)
                    condition.setValue(1);
                else
                    condition.setValue(0);
                break;
            }
            case Integer i: {
                condition.setValue(i);
                break;
            }
            default: {
                condition.setValue(null);
            }
        }
        return condition;
    }

    private Action addAction(DeviceActionAvro actionAvro) {
        Action action = new Action();
        action.setType(actionAvro.getType());
        action.setValue(actionAvro.getValue());
        return action;
    }

    private List<Scenario> getScenariosBySensorIdInConditions(String sensorId, String hubId) {
        return scenarios.stream()
                .filter(scenario -> scenario.getHubId().equals(hubId))
                .filter(scenario -> scenario.getConditions().containsKey(sensorId))
                .toList();
    }

    private List<Scenario> getScenariosBySensorIdInActions(String sensorId, String hubId) {
        return scenarios.stream()
                .filter(scenario -> scenario.getHubId().equals(hubId))
                .filter(scenario -> scenario.getActions().containsKey(sensorId))
                .toList();
    }

}
