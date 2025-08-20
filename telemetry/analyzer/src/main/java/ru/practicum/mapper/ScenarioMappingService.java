package ru.practicum.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.model.Scenario;
import ru.practicum.repositories.SensorRepository;
import ru.yandex.practicum.kafka.telemetry.event.ScenarioAddedEventAvro;

@Service
@RequiredArgsConstructor
public class ScenarioMappingService {
    private final MapperClass mapper;
    private final SensorRepository sensorRepository;

    public Scenario completeScenarioMapping(ScenarioAddedEventAvro avro, String hubId) {
        Scenario scenario = mapper.mapFromAvro(avro, hubId);

        // Устанавливаем связи для условий
        if (scenario.getConditions() != null) {
            scenario.getConditions().forEach(condition -> {
                if (condition.getSensor() != null && condition.getSensor().getId() != null) {
                    sensorRepository.findById(condition.getSensor().getId())
                            .ifPresent(condition::setSensor);
                }
            });
        }

        // Устанавливаем связи для действий
        if (scenario.getActions() != null) {
            scenario.getActions().forEach(action -> {
                if (action.getSensor() != null && action.getSensor().getId() != null) {
                    sensorRepository.findById(action.getSensor().getId())
                            .ifPresent(action::setSensor);
                }
            });
        }

        return scenario;
    }
}
