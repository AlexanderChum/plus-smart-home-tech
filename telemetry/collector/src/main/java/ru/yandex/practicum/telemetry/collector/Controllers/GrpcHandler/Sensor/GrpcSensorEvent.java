package ru.yandex.practicum.telemetry.collector.Controllers.GrpcHandler.Sensor;

import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;

public interface GrpcSensorEvent {
    SensorEventProto.PayloadCase getMessageType();

    void handle(SensorEventProto event);
}
