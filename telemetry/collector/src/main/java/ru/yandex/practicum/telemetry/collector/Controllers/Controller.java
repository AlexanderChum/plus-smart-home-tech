package ru.yandex.practicum.telemetry.collector.Controllers;

import com.google.protobuf.Empty;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import ru.yandex.practicum.grpc.telemetry.collector.CollectorControllerGrpc;
import ru.yandex.practicum.grpc.telemetry.event.HubEventProto;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;
import ru.yandex.practicum.telemetry.collector.Controllers.GrpcHandler.Device.GrpcDeviceEvent;
import ru.yandex.practicum.telemetry.collector.Controllers.GrpcHandler.Sensor.GrpcSensorEvent;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@GrpcService
public class Controller extends CollectorControllerGrpc.CollectorControllerImplBase {
    private final Map<HubEventProto.PayloadCase, GrpcDeviceEvent> deviceHandlers;
    private final Map<SensorEventProto.PayloadCase, GrpcSensorEvent> sensorHandlers;

    public Controller(Set<GrpcDeviceEvent> deviceHandlers, Set<GrpcSensorEvent> sensorHandlers) {
        this.deviceHandlers = deviceHandlers.stream()
                .collect(Collectors.toMap(GrpcDeviceEvent::getMessageType, Function.identity()));
        this.sensorHandlers = sensorHandlers.stream()
                .collect(Collectors.toMap(GrpcSensorEvent::getMessageType, Function.identity()));
    }

    public void collectSensorEvent(SensorEventProto request, StreamObserver<Empty> response) {
        try {
            GrpcSensorEvent event = sensorHandlers.get(request.getPayloadCase());
            event.handle(request);
            response.onNext(Empty.getDefaultInstance());
            response.onCompleted();
        } catch (Exception e) {
            response.onError(new StatusRuntimeException(
                    Status.INTERNAL
                            .withDescription(e.getLocalizedMessage())
                            .withCause(e)
            ));
        }
    }

    public void collectHubEvent(HubEventProto request, StreamObserver<Empty> response) {
        try {
            GrpcDeviceEvent event = deviceHandlers.get(request.getPayloadCase());
            event.handle(request);
            response.onNext(Empty.getDefaultInstance());
            response.onCompleted();
        } catch (Exception e) {
            response.onError(new StatusRuntimeException(
                    Status.INTERNAL
                            .withDescription(e.getLocalizedMessage())
                            .withCause(e)
            ));
        }
    }
}
