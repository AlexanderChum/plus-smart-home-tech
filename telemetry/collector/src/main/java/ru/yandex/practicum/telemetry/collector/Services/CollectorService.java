package ru.yandex.practicum.telemetry.collector.Services;

import org.springframework.http.ResponseEntity;
import ru.yandex.practicum.telemetry.collector.Models.HubEvent.HubEvent;
import ru.yandex.practicum.telemetry.collector.Models.SensorEvent.SensorEvent;

public interface CollectorService {
    ResponseEntity<Void> sendSensorData(SensorEvent event);

    ResponseEntity<Void> sendHubData(HubEvent event);
}
