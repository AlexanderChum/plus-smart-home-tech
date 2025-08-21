package ru.practicum;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.errors.WakeupException;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;

import java.time.Duration;
import java.util.List;

import static ru.practicum.AggregatorConsumer.SENSOR_TOPIC;
import static ru.practicum.AggregatorProducer.SNAPSHOT_TOPIC;

@Slf4j
@Component
@RequiredArgsConstructor
public class AggregationStarter {
    private final AggregatorConsumer consumerFactory;
    private final AggregatorProducer producerFactory;
    private final AggregationService service;

    public void start() {
        KafkaConsumer<String, SpecificRecordBase> consumer = consumerFactory.createConsumer();
        KafkaProducer<String, SpecificRecordBase> producer = producerFactory.createProducer();

        try {
            consumer.subscribe(List.of(SENSOR_TOPIC));

            while (true) {
                ConsumerRecords<String, SpecificRecordBase> records = consumer.poll(Duration.ofSeconds(1));

                records.forEach(record -> {
                    try {
                        SensorEventAvro event = (SensorEventAvro) record.value();
                        service.updateState(event).ifPresent(snapshot -> {
                            producer.send(new ProducerRecord<>(SNAPSHOT_TOPIC, snapshot.getHubId(), snapshot));
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
                producer.flush();
                consumer.commitSync();
            } finally {
                log.info("Закрываем консьюмер");
                consumer.close();
                log.info("Закрываем продюсер");
                producer.close();
            }
        }
    }
}
