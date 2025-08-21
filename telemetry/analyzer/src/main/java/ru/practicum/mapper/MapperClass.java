package ru.practicum.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import ru.practicum.model.Action;
import ru.practicum.model.Condition;
import ru.practicum.model.Scenario;
import ru.yandex.practicum.grpc.telemetry.event.DeviceActionProto;
import ru.yandex.practicum.kafka.telemetry.event.DeviceActionAvro;
import ru.yandex.practicum.kafka.telemetry.event.ScenarioAddedEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.ScenarioConditionAvro;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MapperClass {

    @Mapping(target = "sensorId", source = "sensor.id")
    DeviceActionProto mapToProto(Action action);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "scenarios", ignore = true)
    @Mapping(target = "sensor.id", source = "sensorId")
    Action mapFromAvro(DeviceActionAvro avro);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "scenarios", ignore = true)
    @Mapping(target = "sensor.id", source = "sensorId")
    @Mapping(target = "value", source = "value", qualifiedByName = "extractValue")
    Condition mapFromAvro(ScenarioConditionAvro condition);

    List<Condition> mapFromAvro(List<ScenarioConditionAvro> conditions);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "hubId", source = "hubId")
    @Mapping(target = "name", source = "avro.name")
    @Mapping(target = "conditions", source = "avro.conditions")
    @Mapping(target = "actions", source = "avro.actions")
    Scenario mapFromAvro(ScenarioAddedEventAvro avro, String hubId);

    @Named("extractValue")
    default Integer extractValue(Object value) {
        if (value instanceof Integer) return (Integer) value;
        if (value instanceof Boolean) return (Boolean) value ? 1 : 0;
        throw new IllegalArgumentException("Unsupported type: " + value.getClass());
    }
}
