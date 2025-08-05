package ru.yandex.practicum.telemetry.collector.Models.SensorEvent.SensorEventImpls;

import ru.yandex.practicum.telemetry.collector.Models.SensorEvent.SensorEvent;
import ru.yandex.practicum.telemetry.collector.Models.SensorEvent.SensorEventType;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper = true)
public class SwitchSensorEvent extends SensorEvent {
    @NotNull
    Boolean state;

    @Override
    public SensorEventType getType() {
        return SensorEventType.SWITCH_SENSOR_EVENT;
    }
}
