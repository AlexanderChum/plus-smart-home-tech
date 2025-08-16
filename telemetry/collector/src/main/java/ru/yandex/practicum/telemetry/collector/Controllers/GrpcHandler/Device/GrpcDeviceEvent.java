package ru.yandex.practicum.telemetry.collector.Controllers.GrpcHandler.Device;

import ru.yandex.practicum.grpc.telemetry.event.HubEventProto;

public interface GrpcDeviceEvent {
    HubEventProto.PayloadCase getMessageType();

    void handle(HubEventProto event);
}
