package ru.yandex.practicum.telemetry.collector.Mapper;

import ru.yandex.practicum.telemetry.collector.Models.HubEvent.DeviceEventImpl.DeviceAddedEvent;
import ru.yandex.practicum.telemetry.collector.Models.HubEvent.DeviceEventImpl.DeviceRemovedEvent;
import ru.yandex.practicum.telemetry.collector.Models.HubEvent.ScenarioEventImpl.ScenarioAddedEvent;
import ru.yandex.practicum.telemetry.collector.Models.HubEvent.ScenarioEventImpl.ScenarioRemovedEvent;
import ru.yandex.practicum.telemetry.collector.Models.SensorEvent.SensorEventImpls.ClimateSensorEvent;
import ru.yandex.practicum.telemetry.collector.Models.SensorEvent.SensorEventImpls.LightSensorEvent;
import ru.yandex.practicum.telemetry.collector.Models.SensorEvent.SensorEventImpls.MotionSensorEvent;
import ru.yandex.practicum.telemetry.collector.Models.SensorEvent.SensorEventImpls.SwitchSensorEvent;
import ru.yandex.practicum.telemetry.collector.Models.SensorEvent.SensorEventImpls.TemperatureSensorEvent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.yandex.practicum.kafka.telemetry.event.ClimateSensorAvro;
import ru.yandex.practicum.kafka.telemetry.event.ConditionOperationAvro;
import ru.yandex.practicum.kafka.telemetry.event.ConditionTypeAvro;
import ru.yandex.practicum.kafka.telemetry.event.DeviceAddedEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.DeviceRemovedEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.LightSensorAvro;
import ru.yandex.practicum.kafka.telemetry.event.MotionSensorAvro;
import ru.yandex.practicum.kafka.telemetry.event.ScenarioAddedEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.ScenarioRemovedEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.SwitchSensorAvro;
import ru.yandex.practicum.kafka.telemetry.event.TemperatureSensorAvro;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.ScenarioConditionAvro;
import ru.yandex.practicum.kafka.telemetry.event.DeviceActionAvro;
import ru.yandex.practicum.kafka.telemetry.event.DeviceTypeAvro;
import ru.yandex.practicum.kafka.telemetry.event.ActionTypeAvro;

import ru.yandex.practicum.telemetry.collector.Models.HubEvent.ScenarioEventImpl.ScenatioCondition;
import ru.yandex.practicum.telemetry.collector.Models.Enums.DeviceAction;

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

    default ClimateSensorAvro toClimateSensorPayload(ClimateSensorEvent event) {
        ClimateSensorAvro payload = new ClimateSensorAvro();
        payload.setTemperatureC(event.getTemperatureC());
        payload.setHumidity(event.getHumidity());
        payload.setCo2Level(event.getCo2Level());
        return payload;
    }

    default LightSensorAvro toLightSensorPayload(LightSensorEvent event) {
        LightSensorAvro payload = new LightSensorAvro();
        payload.setLinkQuality(event.getLinkQuality());
        payload.setLuminosity(event.getLuminosity());
        return payload;
    }

    default MotionSensorAvro toMotionSensorPayload(MotionSensorEvent event) {
        MotionSensorAvro payload = new MotionSensorAvro();
        payload.setLinkQuality(event.getLinkQuality());
        payload.setMotion(event.getMotion());
        payload.setVoltage(event.getVoltage());
        return payload;
    }

    default SwitchSensorAvro toSwitchSensorPayload(SwitchSensorEvent event) {
        SwitchSensorAvro payload = new SwitchSensorAvro();
        payload.setState(event.getState());
        return payload;
    }

    default TemperatureSensorAvro toTemperatureSensorPayload(TemperatureSensorEvent event) {
        TemperatureSensorAvro payload = new TemperatureSensorAvro();
        payload.setTemperatureC(event.getTemperatureC());
        payload.setTemperatureF(event.getTemperatureF());
        return payload;
    }

    default DeviceAddedEventAvro toDeviceAddedEventPayload(DeviceAddedEvent event) {
        DeviceAddedEventAvro payload = new DeviceAddedEventAvro();
        payload.setId(event.getId());
        payload.setType(DeviceTypeAvro.valueOf(event.getDeviceType().name()));
        return payload;
    }

    default DeviceRemovedEventAvro toDeviceRemovedEventPayload(DeviceRemovedEvent event) {
        DeviceRemovedEventAvro payload = new DeviceRemovedEventAvro();
        payload.setId(event.getId());
        return payload;
    }

    default ScenarioAddedEventAvro toScenarioAddedEventPayload(ScenarioAddedEvent event) {
        ScenarioAddedEventAvro payload = new ScenarioAddedEventAvro();
        payload.setName(event.getName());
        payload.setConditions(event.getConditions().stream()
                .map(this::toScenarioConditionAvro)
                .toList());
        payload.setActions(event.getActions().stream()
                .map(this::toDeviceActionAvro)
                .toList());
        return payload;
    }

    default ScenarioRemovedEventAvro toScenarioRemovedEventPayload(ScenarioRemovedEvent event) {
        ScenarioRemovedEventAvro payload = new ScenarioRemovedEventAvro();
        payload.setName(event.getName());
        return payload;
    }

    default ScenarioConditionAvro toScenarioConditionAvro(ScenatioCondition condition) {
        ScenarioConditionAvro avroCondition = new ScenarioConditionAvro();
        avroCondition.setSensorId(condition.getSensorId());
        avroCondition.setType(ConditionTypeAvro.valueOf(condition.getType().name()));
        avroCondition.setOperation(ConditionOperationAvro.valueOf(condition.getOperation().name()));
        avroCondition.setValue(condition.getValue());
        return avroCondition;
    }

    default DeviceActionAvro toDeviceActionAvro(DeviceAction action) {
        DeviceActionAvro avroAction = new DeviceActionAvro();
        avroAction.setSensorId("");
        avroAction.setType(ActionTypeAvro.valueOf(action.name()));
        avroAction.setValue(null);
        return avroAction;
    }
}
