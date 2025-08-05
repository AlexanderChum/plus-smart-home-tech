package ru.yandex.practicum.telemetry.collector.Services;

import ru.yandex.practicum.telemetry.collector.KafkaProducer.TelemetryProducerConfig;
import ru.yandex.practicum.telemetry.collector.Mapper.EventsMapper;
import ru.yandex.practicum.telemetry.collector.Models.HubEvent.HubEvent;
import ru.yandex.practicum.telemetry.collector.Models.HubEvent.DeviceEventImpl.DeviceAddedEvent;
import ru.yandex.practicum.telemetry.collector.Models.HubEvent.DeviceEventImpl.DeviceRemovedEvent;
import ru.yandex.practicum.telemetry.collector.Models.HubEvent.ScenarioEventImpl.ScenarioAddedEvent;
import ru.yandex.practicum.telemetry.collector.Models.HubEvent.ScenarioEventImpl.ScenarioRemovedEvent;
import ru.yandex.practicum.telemetry.collector.Models.SensorEvent.SensorEvent;
import ru.yandex.practicum.telemetry.collector.Models.SensorEvent.SensorEventImpls.ClimateSensorEvent;
import ru.yandex.practicum.telemetry.collector.Models.SensorEvent.SensorEventImpls.LightSensorEvent;
import ru.yandex.practicum.telemetry.collector.Models.SensorEvent.SensorEventImpls.MotionSensorEvent;
import ru.yandex.practicum.telemetry.collector.Models.SensorEvent.SensorEventImpls.SwitchSensorEvent;
import ru.yandex.practicum.telemetry.collector.Models.SensorEvent.SensorEventImpls.TemperatureSensorEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;

@Slf4j
@RequiredArgsConstructor
@Service
public class CollectorServiceImpl implements CollectorService {
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final EventsMapper eventsMapper;
    private final TelemetryProducerConfig kafka;

    @Override
    public void sendSensorData(SensorEvent sensorEvent) {
        if (sensorEvent == null) {
            log.error("Событие сенсора не может быть null");
            return;
        }

        String sensorTopic = kafka.getSensorTopic();
        log.info("Отправка события сенсора типа: {}", sensorEvent.getType());

        try {
            SensorEventAvro avroSensorEvent = switch (sensorEvent) {
                case ClimateSensorEvent climateEvent -> {
                    SensorEventAvro event = eventsMapper.toAvro(climateEvent);
                    event.setPayload(eventsMapper.toClimateSensorPayload(climateEvent));
                    yield event;
                }
                case LightSensorEvent lightEvent -> {
                    SensorEventAvro event = eventsMapper.toAvro(lightEvent);
                    event.setPayload(eventsMapper.toLightSensorPayload(lightEvent));
                    yield event;
                }
                case MotionSensorEvent motionEvent -> {
                    SensorEventAvro event = eventsMapper.toAvro(motionEvent);
                    event.setPayload(eventsMapper.toMotionSensorPayload(motionEvent));
                    yield event;
                }
                case SwitchSensorEvent switchEvent -> {
                    SensorEventAvro event = eventsMapper.toAvro(switchEvent);
                    event.setPayload(eventsMapper.toSwitchSensorPayload(switchEvent));
                    yield event;
                }
                case TemperatureSensorEvent temperatureEvent -> {
                    SensorEventAvro event = eventsMapper.toAvro(temperatureEvent);
                    event.setPayload(eventsMapper.toTemperatureSensorPayload(temperatureEvent));
                    yield event;
                }
                default -> throw new IllegalArgumentException("Неизвестный тип события: " + sensorEvent.getType());
            };

            String key = avroSensorEvent.getHubId();
            
            kafkaTemplate.send(sensorTopic, key, avroSensorEvent)
                    .whenComplete((result, exception) -> {
                        if (exception == null) {
                            log.info("Событие сенсора успешно отправлено в топик {}", sensorTopic);
                        } else {
                            log.error("Не удалось отправить событие сенсора: {}", exception.getMessage(), exception);
                        }
                    });
        } catch (Exception e) {
            log.error("Ошибка при обработке события сенсора: {}", e.getMessage(), e);
            throw new RuntimeException("Ошибка при обработке события сенсора", e);
        }
    }

    @Override
    public void sendHubData(HubEvent hubEvent) {
        if (hubEvent == null) {
            log.error("Событие хаба не может быть null");
            return;
        }

        String hubTopic = kafka.getHubTopic();
        log.info("Отправка события хаба типа: {}", hubEvent.getType());

        try {
            HubEventAvro avroHubEvent = switch (hubEvent) {
                case DeviceAddedEvent deviceAddedEvent -> {
                    HubEventAvro event = eventsMapper.toAvro(deviceAddedEvent);
                    event.setPayload(eventsMapper.toDeviceAddedEventPayload(deviceAddedEvent));
                    yield event;
                }
                case DeviceRemovedEvent deviceRemovedEvent -> {
                    HubEventAvro event = eventsMapper.toAvro(deviceRemovedEvent);
                    event.setPayload(eventsMapper.toDeviceRemovedEventPayload(deviceRemovedEvent));
                    yield event;
                }
                case ScenarioAddedEvent scenarioAddedEvent -> {
                    HubEventAvro event = eventsMapper.toAvro(scenarioAddedEvent);
                    event.setPayload(eventsMapper.toScenarioAddedEventPayload(scenarioAddedEvent));
                    yield event;
                }
                case ScenarioRemovedEvent scenarioRemovedEvent -> {
                    HubEventAvro event = eventsMapper.toAvro(scenarioRemovedEvent);
                    event.setPayload(eventsMapper.toScenarioRemovedEventPayload(scenarioRemovedEvent));
                    yield event;
                }
                default -> throw new IllegalArgumentException("Неизвестный тип события хаба: " + hubEvent.getType());
            };

            String key = avroHubEvent.getHubId();
            
            kafkaTemplate.send(hubTopic, key, avroHubEvent)
                    .whenComplete((result, exception) -> {
                        if (exception == null) {
                            log.info("Событие хаба успешно отправлено в топик {}", hubTopic);
                        } else {
                            log.error("Не удалось отправить событие хаба: {}", exception.getMessage(), exception);
                        }
                    });
        } catch (Exception e) {
            log.error("Ошибка при обработке события хаба: {}", e.getMessage(), e);
            throw new RuntimeException("Ошибка при обработке события хаба", e);
        }
    }
}
