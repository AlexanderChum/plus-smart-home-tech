package ru.practicum.processors;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.errors.WakeupException;
import org.springframework.stereotype.Component;
import ru.practicum.kafkaConsumer.ConsumerFactory;
import ru.practicum.mapper.MapperClass;
import ru.practicum.model.Scenario;
import ru.practicum.model.Sensor;
import ru.practicum.repositories.ScenarioRepository;
import ru.practicum.repositories.SensorRepository;
import ru.yandex.practicum.kafka.telemetry.event.DeviceAddedEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.DeviceRemovedEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.ScenarioAddedEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.ScenarioRemovedEventAvro;

import java.time.Duration;
import java.util.List;

import static ru.practicum.kafkaConsumer.ConsumerFactory.HUB_TOPIC;

@Slf4j
@Component
@RequiredArgsConstructor
public class HubEventProcessor implements Runnable {
    private final ConsumerFactory factory;
    private final MapperClass mapper;
    private final SensorRepository sensorRepository;
    private final ScenarioRepository scenarioRepository;

    @Override
    public void run() {
        KafkaConsumer<String, SpecificRecordBase> consumer = factory.createHubConsumer();

        try {
            consumer.subscribe(List.of(HUB_TOPIC));

            while (true) {
                ConsumerRecords<String, SpecificRecordBase> records = consumer.poll(Duration.ofSeconds(1));

                records.forEach(record -> {
                    try {
                        HubEventAvro event = (HubEventAvro) record.value();
                        SpecificRecordBase payload = (SpecificRecordBase) event.getPayload();

                        switch (payload) {
                            case DeviceAddedEventAvro deviceAvro -> {
                                Sensor sensor = new Sensor();
                                sensor.setId(deviceAvro.getId());
                                sensor.setHubId(event.getHubId());
                                sensorRepository.save(sensor);
                            }
                            case DeviceRemovedEventAvro removedAvro -> {
                                sensorRepository.deleteById(removedAvro.getId());
                            }
                            case ScenarioAddedEventAvro scenarioAvro -> {
                                Scenario scenario = mapper.mapFromAvro(scenarioAvro, event.getHubId());
                                scenarioRepository.save(scenario);
                            }
                            case ScenarioRemovedEventAvro removedAvro -> {
                                scenarioRepository.deleteByName(removedAvro.getName());
                            }
                            default -> log.warn("Неизвестный тип события");
                        }
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
}