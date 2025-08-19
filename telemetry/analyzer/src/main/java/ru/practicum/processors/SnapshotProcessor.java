package ru.practicum.processors;

import com.google.protobuf.Timestamp;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.errors.WakeupException;
import org.springframework.stereotype.Component;
import ru.practicum.kafkaConsumer.ConsumerFactory;
import ru.practicum.mapper.MapperClass;
import ru.practicum.model.Condition;
import ru.practicum.model.Scenario;
import ru.practicum.repositories.ScenarioRepository;
import ru.yandex.practicum.grpc.telemetry.event.DeviceActionRequest;
import ru.yandex.practicum.grpc.telemetry.hubrouter.HubRouterControllerGrpc;
import ru.yandex.practicum.kafka.telemetry.event.ClimateSensorAvro;
import ru.yandex.practicum.kafka.telemetry.event.LightSensorAvro;
import ru.yandex.practicum.kafka.telemetry.event.MotionSensorAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorStateAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;
import ru.yandex.practicum.kafka.telemetry.event.SwitchSensorAvro;
import ru.yandex.practicum.kafka.telemetry.event.TemperatureSensorAvro;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static ru.practicum.kafkaConsumer.ConsumerFactory.SNAPSHOT_TOPIC;

@Slf4j
@Component
@RequiredArgsConstructor
public class SnapshotProcessor implements Runnable {
    @GrpcClient("hub-router-controller")
    private final HubRouterControllerGrpc.HubRouterControllerBlockingStub hubRouter;
    private final ConsumerFactory factory;
    private final MapperClass mapper;
    private final ScenarioRepository repository;

    @Override
    public void run() {
        KafkaConsumer<String, SpecificRecordBase> consumer = factory.createSnapshotConsumer();

        try {
            consumer.subscribe(List.of(SNAPSHOT_TOPIC));

            while (true) {
                ConsumerRecords<String, SpecificRecordBase> records = consumer.poll(Duration.ofSeconds(1));

                records.forEach(record -> {
                    try {
                        SensorsSnapshotAvro snapshot = (SensorsSnapshotAvro) record.value();

                        Map<String, SensorStateAvro> sensorStates = snapshot.getSensorsState();
                        if (sensorStates == null || sensorStates.isEmpty()) {
                            return;
                        }

                        List<Scenario> scenarios = repository.findByHubId(snapshot.getHubId());

                        List<Scenario> triggeredScenarios = scenarios.stream()
                                .filter(scenario ->
                                        scenario.getConditions().stream()
                                                .allMatch(condition ->
                                                        checkCondition(
                                                                condition,
                                                                sensorStates.get(condition.getSensor().getId())
                                                        )
                                                )
                                )
                                .toList();

                        triggeredScenarios.forEach(scenario -> {
                            Timestamp timestamp = Timestamp.newBuilder()
                                    .setSeconds(Instant.now().getEpochSecond())
                                    .setNanos(Instant.now().getNano())
                                    .build();

                            scenario.getActions().stream()
                                    .map(mapper::mapToProto)
                                    .map(actionProto -> DeviceActionRequest.newBuilder()
                                            .setHubId(scenario.getHubId())
                                            .setScenarioName(scenario.getName())
                                            .setAction(actionProto)
                                            .setTimestamp(timestamp)
                                            .build())
                                    .forEach(hubRouter::handleDeviceAction);
                        });

                    } catch (Exception e) {
                        log.error("Ошибка обработки записи", e);
                    }
                });

                consumer.commitSync();
            }

        } catch (WakeupException ignored) {
            log.error("Ошибка WakeupException");
        } catch (Exception e) {
            log.error("Ошибка во время обработки событий от датчиков", e);
        } finally {
            try {
                consumer.commitSync();
            } finally {
                log.info("Закрываем консьюмер");
                consumer.close();
            }
        }
    }

    private boolean checkCondition(Condition condition, SensorStateAvro sensorState) {
        if (condition == null || sensorState == null || sensorState.getData() == null ||
                condition.getType() == null || condition.getOperation() == null || condition.getValue() == null) {
            return false;
        }

        Integer currentValue = switch (condition.getType()) {
            case MOTION -> ((MotionSensorAvro) sensorState.getData()).getMotion() ? 1 : 0;
            case LUMINOSITY -> ((LightSensorAvro) sensorState.getData()).getLuminosity();
            case SWITCH -> ((SwitchSensorAvro) sensorState.getData()).getState() ? 1 : 0;
            case TEMPERATURE -> sensorState.getData() instanceof ClimateSensorAvro ?
                    ((ClimateSensorAvro) sensorState.getData()).getTemperatureC() :
                    ((TemperatureSensorAvro) sensorState.getData()).getTemperatureC();
            case CO2LEVEL -> ((ClimateSensorAvro) sensorState.getData()).getCo2Level();
            case HUMIDITY -> ((ClimateSensorAvro) sensorState.getData()).getHumidity();
        };

        return switch (condition.getOperation()) {
            case EQUALS -> Objects.equals(currentValue, condition.getValue());
            case GREATER_THAN -> currentValue > condition.getValue();
            case LOWER_THAN -> currentValue < condition.getValue();
        };
    }
}
