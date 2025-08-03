package Mapper;

import Models.AvroModels.HubEventAvro;
import Models.AvroModels.SensorEventAvro;
import Models.HubEvent.DeviceEventImpl.DeviceAddedEvent;
import Models.HubEvent.DeviceEventImpl.DeviceRemovedEvent;
import Models.HubEvent.ScenarioEventImpl.ScenarioAddedEvent;
import Models.HubEvent.ScenarioEventImpl.ScenarioRemovedEvent;
import Models.SensorEvent.SensorEventImpls.ClimateSensorEvent;
import Models.SensorEvent.SensorEventImpls.LightSensorEvent;
import Models.SensorEvent.SensorEventImpls.MotionSensorEvent;
import Models.SensorEvent.SensorEventImpls.SwitchSensorEvent;
import Models.SensorEvent.SensorEventImpls.TemperatureSensorEvent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.HashMap;
import java.util.Map;

@Mapper(componentModel = "spring")
public interface EventsMapper {

    @Mapping(target = "id", source = "id")
    @Mapping(target = "hubId", source = "hubId")
    @Mapping(target = "timestamp", source = "timestamp")
    @Mapping(target = "payload", ignore = true)
    SensorEventAvro toAvro(ClimateSensorEvent event);
    
    @Mapping(target = "id", source = "id")
    @Mapping(target = "hubId", source = "hubId")
    @Mapping(target = "timestamp", source = "timestamp")
    @Mapping(target = "payload", ignore = true)
    SensorEventAvro toAvro(LightSensorEvent event);
    
    @Mapping(target = "id", source = "id")
    @Mapping(target = "hubId", source = "hubId")
    @Mapping(target = "timestamp", source = "timestamp")
    @Mapping(target = "payload", ignore = true)
    SensorEventAvro toAvro(MotionSensorEvent event);
    
    @Mapping(target = "id", source = "id")
    @Mapping(target = "hubId", source = "hubId")
    @Mapping(target = "timestamp", source = "timestamp")
    @Mapping(target = "payload", ignore = true)
    SensorEventAvro toAvro(SwitchSensorEvent event);
    
    @Mapping(target = "id", source = "id")
    @Mapping(target = "hubId", source = "hubId")
    @Mapping(target = "timestamp", source = "timestamp")
    @Mapping(target = "payload", ignore = true)
    SensorEventAvro toAvro(TemperatureSensorEvent event);

    @Mapping(target = "hubId", source = "hubId")
    @Mapping(target = "timestamp", source = "timestamp")
    @Mapping(target = "payload", ignore = true)
    HubEventAvro toAvro(DeviceAddedEvent event);
    
    @Mapping(target = "hubId", source = "hubId")
    @Mapping(target = "timestamp", source = "timestamp")
    @Mapping(target = "payload", ignore = true)
    HubEventAvro toAvro(DeviceRemovedEvent event);
    
    @Mapping(target = "hubId", source = "hubId")
    @Mapping(target = "timestamp", source = "timestamp")
    @Mapping(target = "payload", ignore = true)
    HubEventAvro toAvro(ScenarioAddedEvent event);
    
    @Mapping(target = "hubId", source = "hubId")
    @Mapping(target = "timestamp", source = "timestamp")
    @Mapping(target = "payload", ignore = true)
    HubEventAvro toAvro(ScenarioRemovedEvent event);

    // Default methods for payload conversion
    default Map<String, Object> toClimateSensorPayload(ClimateSensorEvent event) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("type", "CLIMATE_SENSOR");
        payload.put("temperatureC", event.getTemperatureC());
        payload.put("humidity", event.getHumidity());
        payload.put("co2Level", event.getCo2Level());
        return payload;
    }

    default Map<String, Object> toLightSensorPayload(LightSensorEvent event) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("type", "LIGHT_SENSOR");
        payload.put("linkQuality", event.getLinkQuality());
        payload.put("luminosity", event.getLuminosity());
        return payload;
    }

    default Map<String, Object> toMotionSensorPayload(MotionSensorEvent event) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("type", "MOTION_SENSOR");
        payload.put("linkQuality", event.getLinkQuality());
        payload.put("motion", event.getMotion());
        payload.put("voltage", event.getVoltage());
        return payload;
    }

    default Map<String, Object> toSwitchSensorPayload(SwitchSensorEvent event) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("type", "SWITCH_SENSOR");
        payload.put("state", event.getState());
        return payload;
    }

    default Map<String, Object> toTemperatureSensorPayload(TemperatureSensorEvent event) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("type", "TEMPERATURE_SENSOR");
        payload.put("temperatureC", event.getTemperatureC());
        payload.put("temperatureF", event.getTemperatureF());
        return payload;
    }

    default Map<String, Object> toDeviceAddedEventPayload(DeviceAddedEvent event) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("type", "DEVICE_ADDED");
        payload.put("id", event.getId());
        payload.put("deviceType", event.getDeviceType().name());
        return payload;
    }

    default Map<String, Object> toDeviceRemovedEventPayload(DeviceRemovedEvent event) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("type", "DEVICE_REMOVED");
        payload.put("id", event.getId());
        return payload;
    }

    default Map<String, Object> toScenarioAddedEventPayload(ScenarioAddedEvent event) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("type", "SCENARIO_ADDED");
        payload.put("name", event.getName());
        payload.put("conditions", event.getConditions());
        payload.put("actions", event.getActions());
        return payload;
    }

    default Map<String, Object> toScenarioRemovedEventPayload(ScenarioRemovedEvent event) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("type", "SCENARIO_REMOVED");
        payload.put("name", event.getName());
        return payload;
    }
}
