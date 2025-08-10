package ru.yandex.practicum.telemetry.collector.Models.SensorEvent.SensorEventImpls;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.yandex.practicum.telemetry.collector.Models.SensorEvent.SensorEvent;
import ru.yandex.practicum.telemetry.collector.Models.SensorEvent.SensorEventType;

@Getter
@Setter
@ToString(callSuper = true)
public class SwitchSensorEvent extends SensorEvent {
    @NotNull
    private Boolean state;

    @Override
    public SensorEventType getType() {
        return SensorEventType.SWITCH_SENSOR_EVENT;
    }
}
