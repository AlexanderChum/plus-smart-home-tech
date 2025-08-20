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

    // Маппинг Action -> DeviceActionProto
    @Mapping(target = "sensorId", source = "sensor.id")
    DeviceActionProto mapToProto(Action action);

    // Маппинг DeviceActionAvro -> Action
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "scenarios", ignore = true)
    @Mapping(target = "sensor.id", source = "sensorId")
    Action mapFromAvro(DeviceActionAvro avro);

    // Маппинг ScenarioConditionAvro -> Condition
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "scenarios", ignore = true)
    @Mapping(target = "sensor.id", source = "sensorId")
    @Mapping(target = "value", source = "value", qualifiedByName = "extractValue")
    Condition mapFromAvro(ScenarioConditionAvro condition);

    // Маппинг списка ScenarioConditionAvro -> List<Condition>
    List<Condition> mapFromAvro(List<ScenarioConditionAvro> conditions);

    // Маппинг ScenarioAddedEventAvro -> Scenario
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "hubId", source = "hubId")
    @Mapping(target = "name", source = "avro.name")
    @Mapping(target = "conditions", source = "avro.conditions")
    @Mapping(target = "actions", source = "avro.actions")
    Scenario mapFromAvro(ScenarioAddedEventAvro avro, String hubId);

    // Метод для извлечения значения из union-поля Avro
    @Named("extractValue")
    default Integer extractValue(Object value) {
        if (value instanceof Integer) return (Integer) value;
        if (value instanceof Boolean) return (Boolean) value ? 1 : 0;
        throw new IllegalArgumentException("Unsupported type: " + value.getClass());
    }
}
