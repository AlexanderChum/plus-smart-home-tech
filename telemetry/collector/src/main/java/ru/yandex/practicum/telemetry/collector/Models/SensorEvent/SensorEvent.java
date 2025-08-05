package ru.yandex.practicum.telemetry.collector.Models.SensorEvent;

import ru.yandex.practicum.telemetry.collector.Models.SensorEvent.SensorEventImpls.ClimateSensorEvent;
import ru.yandex.practicum.telemetry.collector.Models.SensorEvent.SensorEventImpls.LightSensorEvent;
import ru.yandex.practicum.telemetry.collector.Models.SensorEvent.SensorEventImpls.MotionSensorEvent;
import ru.yandex.practicum.telemetry.collector.Models.SensorEvent.SensorEventImpls.SwitchSensorEvent;
import ru.yandex.practicum.telemetry.collector.Models.SensorEvent.SensorEventImpls.TemperatureSensorEvent;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.Instant;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        property = "type",
        defaultImpl = SensorEventType.class
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = LightSensorEvent.class, name = "LIGHT_SENSOR_EVENT"),
        @JsonSubTypes.Type(value = MotionSensorEvent.class, name = "MOTION_SENSOR_EVENT"),
        @JsonSubTypes.Type(value = TemperatureSensorEvent.class, name = "TEMPERATURE_EVENT_CLASS"),
        @JsonSubTypes.Type(value = ClimateSensorEvent.class, name = "CLIMATE_SENSOR_EVENT"),
        @JsonSubTypes.Type(value = SwitchSensorEvent.class, name = "SWITCH_SENSOR_EVENT")
})
@Getter
@Setter
@ToString
public abstract class SensorEvent {
    @NotBlank
    private String id;
    @NotBlank
    private String hubId;
    private Instant timestamp = Instant.now();

    @NotNull
    public abstract SensorEventType getType();
}
