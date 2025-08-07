package ru.yandex.practicum.telemetry.collector.Models.HubEvent.DeviceEventImpl;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.yandex.practicum.telemetry.collector.Models.HubEvent.Enums.ActionType;

@Getter
@Setter
@ToString
public class DeviceAction {
    private String sensorId;
    private ActionType type;
    private Integer value;
}
