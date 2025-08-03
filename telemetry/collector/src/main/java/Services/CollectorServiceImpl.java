package Services;

import KafkaProducer.TelemetryProducerConfig;
import Mapper.EventsMapper;
import Models.HubEvent.HubEvent;
import Models.HubEvent.DeviceEventImpl.DeviceAddedEvent;
import Models.HubEvent.DeviceEventImpl.DeviceRemovedEvent;
import Models.HubEvent.ScenarioEventImpl.ScenarioAddedEvent;
import Models.HubEvent.ScenarioEventImpl.ScenarioRemovedEvent;
import Models.SensorEvent.SensorEvent;
import Models.SensorEvent.SensorEventImpls.ClimateSensorEvent;
import Models.SensorEvent.SensorEventImpls.LightSensorEvent;
import Models.SensorEvent.SensorEventImpls.MotionSensorEvent;
import Models.SensorEvent.SensorEventImpls.SwitchSensorEvent;
import Models.SensorEvent.SensorEventImpls.TemperatureSensorEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import Models.AvroModels.HubEventAvro;
import Models.AvroModels.SensorEventAvro;

@Slf4j
@RequiredArgsConstructor
@Service
public class CollectorServiceImpl implements CollectorService {
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final EventsMapper eventsMapper;
    private final TelemetryProducerConfig kafka;

    @Override
    public void sendSensorData(SensorEvent sensorEvent) {
        String sensorTopic = kafka.getSensorTopic();

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

        log.info("Отправка события в топик");

        long eventTimestamp = avroSensorEvent.getTimestamp().toEpochMilli();

        kafkaTemplate.send(sensorTopic, null, eventTimestamp, avroSensorEvent.getHubId(), avroSensorEvent)
                .whenComplete((result, exception) -> {
                    if (exception == null) {
                        log.info("Событие сенсора отправлено");
                    } else {
                        log.error("Не удалось отправить событие сенсора");
                    }
                });
    }

    @Override
    public void sendHubData(HubEvent hubEvent) {
        String hubTopic = kafka.getHubTopic();
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

        log.info("Отправка события в топик");

        long eventTimestamp = avroHubEvent.getTimestamp().toEpochMilli();

        kafkaTemplate.send(hubTopic, null, eventTimestamp, avroHubEvent.getHubId(), avroHubEvent)
                .whenComplete((result, exception) -> {
                    if (exception == null) {
                        log.info("Событие хаба отправлено");
                    } else {
                        log.error("Не удалось отправить событие хаба");
                    }
                });
    }
}
