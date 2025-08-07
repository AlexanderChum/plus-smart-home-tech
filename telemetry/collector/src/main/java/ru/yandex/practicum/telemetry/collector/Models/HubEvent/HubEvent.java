package ru.yandex.practicum.telemetry.collector.Models.HubEvent;

import ru.yandex.practicum.telemetry.collector.Models.HubEvent.Enums.HubEventType;
import ru.yandex.practicum.telemetry.collector.Models.HubEvent.DeviceEventImpl.DeviceAddedEvent;
import ru.yandex.practicum.telemetry.collector.Models.HubEvent.DeviceEventImpl.DeviceRemovedEvent;
import ru.yandex.practicum.telemetry.collector.Models.HubEvent.ScenarioEventImpl.ScenarioAddedEvent;
import ru.yandex.practicum.telemetry.collector.Models.HubEvent.ScenarioEventImpl.ScenarioRemovedEvent;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.Instant;

@Setter
@Getter
@ToString
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        property = "type",
        defaultImpl = HubEventType.class
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = DeviceAddedEvent.class, name = "DEVICE_ADDED"),
        @JsonSubTypes.Type(value = DeviceRemovedEvent.class, name = "DEVICE_REMOVED"),
        @JsonSubTypes.Type(value = ScenarioAddedEvent.class, name = "SCENARIO_ADDED"),
        @JsonSubTypes.Type(value = ScenarioRemovedEvent.class, name = "SCENARIO_REMOVED"),
})
public abstract class HubEvent {
    @NotBlank
    private String hubId;

    private Instant timestamp = Instant.now();

    @NotNull
    public abstract HubEventType getType();
}
