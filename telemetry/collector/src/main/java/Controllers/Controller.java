package Controllers;

import Models.HubEvent.HubEvent;
import Models.SensorEvent.SensorEvent;
import Services.CollectorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    public void sendSensorData(@Valid @RequestBody SensorEvent event) {
        log.info("Получение события от датчика");
        collectorService.sendSensorData(event);
    }

    @PostMapping("/hubs")
    public void sendHubData(@Valid @RequestBody HubEvent event) {
        log.info("Получение информации для хаба");
        collectorService.sendHubData(event);
    }
}
