package ru.yandex.practicum.telemetry.collector.Services;

import ru.yandex.practicum.telemetry.collector.Models.HubEvent.HubEvent;
import ru.yandex.practicum.telemetry.collector.Models.SensorEvent.SensorEvent;

public interface CollectorService {
    void sendSensorData(SensorEvent event);

    void sendHubData(HubEvent event);
}
