package ru.yandex.practicum.telemetry.collector.Services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;
import ru.yandex.practicum.telemetry.collector.KafkaProducer.TelemetryProducerConfig;

import static ru.yandex.practicum.telemetry.collector.KafkaProducer.TelemetryProducerConfig.HUB_TOPIC;
import static ru.yandex.practicum.telemetry.collector.KafkaProducer.TelemetryProducerConfig.SENSOR_TOPIC;

@Service
@Slf4j
@RequiredArgsConstructor
public class CollectorServiceImpl implements CollectorService {
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final TelemetryProducerConfig config;

    @Override
    public void sendSensorData(SensorEventAvro event) {
        kafkaTemplate.send(SENSOR_TOPIC, null, event.getTimestamp().toEpochMilli(), event.getHubId(), event)
                .whenComplete((result, exception) -> {
                    if (exception == null) {
                        log.info("Событие сенсора успешно отправлено");
                    } else {
                        log.error("Не удалось отправить событие сенсора", exception);
                    }
                });
    }

    @Override
    public void sendHubData(HubEventAvro event) {
        kafkaTemplate.send(HUB_TOPIC, null, event.getTimestamp().toEpochMilli(), event.getHubId(), event)
                .whenComplete((result, exception) -> {
                    if (exception == null) {
                        log.info("Событие хаба успешно отправлено");
                    } else {
                        log.error("Не удалось отправить событие хаба", exception);
                    }
                });
    }
}
