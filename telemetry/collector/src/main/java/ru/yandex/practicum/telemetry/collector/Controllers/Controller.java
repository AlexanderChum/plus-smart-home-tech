package ru.yandex.practicum.telemetry.collector.Controllers;

import ru.yandex.practicum.telemetry.collector.Models.HubEvent.HubEvent;
import ru.yandex.practicum.telemetry.collector.Models.SensorEvent.SensorEvent;
import ru.yandex.practicum.telemetry.collector.Services.CollectorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/events")
public class Controller {
    private final CollectorService collectorService;

    @PostMapping("/sensors")
    public ResponseEntity<Void> sendSensorData(@Valid @RequestBody SensorEvent event) {
        log.info("Получение события от датчика типа: {}", event.getType());
        try {
            collectorService.sendSensorData(event);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error("Ошибка при обработке события сенсора: {}", e.getMessage(), e);
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/hubs")
    public ResponseEntity<Void> sendHubData(@Valid @RequestBody HubEvent event) {
        log.info("Получение информации для хаба типа: {}", event.getType());
        try {
            collectorService.sendHubData(event);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error("Ошибка при обработке события хаба: {}", e.getMessage(), e);
            return ResponseEntity.badRequest().build();
        }
    }
}
