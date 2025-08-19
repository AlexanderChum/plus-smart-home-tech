package ru.practicum.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import ru.practicum.model.Action;
import ru.practicum.model.Condition;
import ru.practicum.model.Scenario;
import ru.yandex.practicum.grpc.telemetry.event.DeviceActionProto;
import ru.yandex.practicum.kafka.telemetry.event.DeviceActionAvro;
import ru.yandex.practicum.kafka.telemetry.event.ScenarioAddedEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.ScenarioConditionAvro;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.ERROR)
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
    @Mapping(target = "value", expression = "java(extractValue(condition.getValue()))")
    Condition mapFromAvro(ScenarioConditionAvro condition);

    @Mapping(target = "id", ignore = true)
    Scenario mapFromAvro(ScenarioAddedEventAvro avro, String hubId);

    default Integer extractValue(Object value) {
        if (value instanceof Integer) return (Integer) value;
        if (value instanceof Boolean) return (Boolean) value ? 1 : 0;
        throw new IllegalArgumentException("Неподходящий тип: " + value.getClass());
    }
}
