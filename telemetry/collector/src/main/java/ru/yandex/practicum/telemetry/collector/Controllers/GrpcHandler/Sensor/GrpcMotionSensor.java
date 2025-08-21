package ru.yandex.practicum.telemetry.collector.Controllers.GrpcHandler.Sensor;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;
import ru.yandex.practicum.telemetry.collector.Mapper.ProtoMapper;
import ru.yandex.practicum.telemetry.collector.Services.CollectorService;

@Component
@RequiredArgsConstructor
public class GrpcMotionSensor implements GrpcSensorEvent {
    private final ProtoMapper mapper;
    private final CollectorService service;

    @Override
    public SensorEventProto.PayloadCase getMessageType() {
        return SensorEventProto.PayloadCase.MOTION_SENSOR_EVENT;
    }

    @Override
    public void handle(SensorEventProto event) {
        SensorEventAvro avro = mapper.toAvro(event);
        service.sendSensorData(avro);
    }
}
