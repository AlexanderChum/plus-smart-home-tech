package ru.yandex.practicum.telemetry.collector.Services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;
import ru.yandex.practicum.telemetry.collector.KafkaProducer.TelemetryProducerConfig;
import ru.yandex.practicum.telemetry.collector.Mapper.EventsMapper;
import ru.yandex.practicum.telemetry.collector.Models.HubEvent.DeviceEventImpl.DeviceAddedEvent;
import ru.yandex.practicum.telemetry.collector.Models.HubEvent.DeviceEventImpl.DeviceRemovedEvent;
import ru.yandex.practicum.telemetry.collector.Models.HubEvent.HubEvent;
import ru.yandex.practicum.telemetry.collector.Models.HubEvent.ScenarioEventImpl.ScenarioAddedEvent;
import ru.yandex.practicum.telemetry.collector.Models.HubEvent.ScenarioEventImpl.ScenarioRemovedEvent;
import ru.yandex.practicum.telemetry.collector.Models.SensorEvent.SensorEvent;
import ru.yandex.practicum.telemetry.collector.Models.SensorEvent.SensorEventImpls.ClimateSensorEvent;
import ru.yandex.practicum.telemetry.collector.Models.SensorEvent.SensorEventImpls.LightSensorEvent;
import ru.yandex.practicum.telemetry.collector.Models.SensorEvent.SensorEventImpls.MotionSensorEvent;
import ru.yandex.practicum.telemetry.collector.Models.SensorEvent.SensorEventImpls.SwitchSensorEvent;
import ru.yandex.practicum.telemetry.collector.Models.SensorEvent.SensorEventImpls.TemperatureSensorEvent;

@Service
@Slf4j
@RequiredArgsConstructor
public class CollectorServiceImpl implements CollectorService{
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final EventsMapper eventMapper;
    private final TelemetryProducerConfig config;

    @Override
    public void sendSensorData(SensorEvent sensorEvent) {
        String sensorTopic = config.getSensorTopic();

        SensorEventAvro avroSensorEvent = switch (sensorEvent) {
            case ClimateSensorEvent climateEvent -> eventMapper.toAvro(climateEvent);
            case LightSensorEvent lightEvent -> eventMapper.toAvro(lightEvent);
            case MotionSensorEvent motionEvent -> eventMapper.toAvro(motionEvent);
            case SwitchSensorEvent switchEvent -> eventMapper.toAvro(switchEvent);
            case TemperatureSensorEvent temperatureEvent -> eventMapper.toAvro(temperatureEvent);
            default -> throw new IllegalArgumentException("Неподдерживаемый тип сенсорного события: "
                    + sensorEvent.getType());
        };

        log.info("Отправка сенсорного события в кафку");

        long eventTimestamp = avroSensorEvent.getTimestamp().toEpochMilli();

        kafkaTemplate.send(sensorTopic, null, eventTimestamp, avroSensorEvent.getHubId(), avroSensorEvent)
                .whenComplete((result, exception) -> {
                    if (exception == null) {
                        log.info("Событие сенсора успешно отправлено");
                    } else {
                        log.error("Не удалось отправить событие сенсора");
                    }
                });
    }

    @Override
    public void sendHubData(HubEvent hubEvent) {
        String hubTopic = config.getHubTopic();
        HubEventAvro avroHubEvent = switch (hubEvent) {
            case DeviceAddedEvent deviceAddedEvent -> eventMapper.toAvro(deviceAddedEvent);
            case DeviceRemovedEvent deviceRemovedEvent -> eventMapper.toAvro(deviceRemovedEvent);
            case ScenarioAddedEvent scenarioAddedEvent -> eventMapper.toAvro(scenarioAddedEvent);
            case ScenarioRemovedEvent scenarioRemovedEvent -> eventMapper.toAvro(scenarioRemovedEvent);
            default -> throw new IllegalArgumentException("Неподдерживаемый тип события хаба: " + hubEvent.getType());
        };

        log.info("Отправка события хаба в кафку");

        long eventTimestamp = avroHubEvent.getTimestamp().toEpochMilli();

        kafkaTemplate.send(hubTopic, null, eventTimestamp, avroHubEvent.getHubId(), avroHubEvent)
                .whenComplete((result, exception) -> {
                    if (exception == null) {
                        log.info("Событие хаба успешно отправлено");
                    } else {
                        log.error("Не удалось отправить событие хаба");
                    }
                });
    }
}
