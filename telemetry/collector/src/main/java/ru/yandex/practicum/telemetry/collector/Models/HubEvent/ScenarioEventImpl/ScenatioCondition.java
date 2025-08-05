package ru.yandex.practicum.telemetry.collector.Models.HubEvent.ScenarioEventImpl;

import ru.yandex.practicum.telemetry.collector.Models.Enums.ConditionType;
import ru.yandex.practicum.telemetry.collector.Models.Enums.Operation;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ScenatioCondition {
    private String sensorId;
    private ConditionType type;
    private Operation operation;
    private Integer value;
}
