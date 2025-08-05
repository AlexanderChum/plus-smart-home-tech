package ru.yandex.practicum.telemetry.collector.Models.HubEvent.DeviceEventImpl;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class DeviceAction {
    private String sensorId;
    private DeviceAction type;
    private Integer value;
}
