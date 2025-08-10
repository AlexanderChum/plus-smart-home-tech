package ru.yandex.practicum.telemetry.collector.Models.HubEvent.ScenarioEventImpl;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.yandex.practicum.telemetry.collector.Models.HubEvent.Enums.ConditionOperation;
import ru.yandex.practicum.telemetry.collector.Models.HubEvent.Enums.ConditionType;

@Getter
@Setter
@ToString
public class ScenarioCondition {
    private String sensorId;
    private ConditionType type;
    private ConditionOperation operation;
    private Integer value;
}
