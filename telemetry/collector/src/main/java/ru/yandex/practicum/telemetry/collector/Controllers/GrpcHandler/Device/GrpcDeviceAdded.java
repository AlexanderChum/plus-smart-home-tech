package ru.yandex.practicum.telemetry.collector.Controllers.GrpcHandler.Device;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.grpc.telemetry.event.HubEventProto;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.telemetry.collector.Mapper.ProtoMapper;
import ru.yandex.practicum.telemetry.collector.Services.CollectorService;

@Component
@RequiredArgsConstructor
public class GrpcDeviceAdded implements GrpcDeviceEvent {
    private final ProtoMapper mapper;
    private final CollectorService service;

    @Override
    public HubEventProto.PayloadCase getMessageType() {
        return HubEventProto.PayloadCase.DEVICE_ADDED;
    }

    @Override
    public void handle(HubEventProto event) {
        HubEventAvro avro = mapper.toAvro(event);
        service.sendHubData(avro);
    }
}
