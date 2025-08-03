package Models.HubEvent;

import Models.Enums.HubEventType;
import Models.HubEvent.DeviceEventImpl.DeviceAddedEvent;
import Models.HubEvent.DeviceEventImpl.DeviceRemovedEvent;
import Models.HubEvent.ScenarioEventImpl.ScenarioAddedEvent;
import Models.HubEvent.ScenarioEventImpl.ScenarioRemovedEvent;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.Instant;

@Getter
@Setter
@ToString
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        property = "true",
        defaultImpl = HubEventType.class
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = DeviceAddedEvent.class, name = "DEVICE_ADDED"),
        @JsonSubTypes.Type(value = DeviceRemovedEvent.class, name = "DEVICE_REMOVED"),
        @JsonSubTypes.Type(value = ScenarioAddedEvent.class, name = "SCENARIO_ADDED"),
        @JsonSubTypes.Type(value = ScenarioRemovedEvent.class, name = "SCENARIO_REMOVED")
})
public abstract class HubEvent {
    @NotBlank
    String hubId;

    Instant timestamp = Instant.now();

    @NotNull
    public abstract HubEventType getType();
}
