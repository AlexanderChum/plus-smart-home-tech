package ru.yandex.practicum.telemetry.collector.KafkaProducer;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;

import java.util.HashMap;
import java.util.Map;

@Configuration
@Slf4j
@Getter
@Setter
public class TelemetryProducerConfig {
    public final static String BOOTSTRAP_SERVERS = "localhost:9092";
    public final static String KEY_SERIALIZER = "org.apache.kafka.common.serialization.StringSerializer";
    public final static String VALUE_SERIALIZER = "ru.practicum.kafka.serializer.AvroSerializer";
    public final static String SENSOR_TOPIC = "telemetry.sensors.v1";
    public final static String HUB_TOPIC = "telemetry.hubs.v1";

    @Bean
    public ProducerFactory<String, Object> producerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, KEY_SERIALIZER);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, VALUE_SERIALIZER);

        log.info("Создание ProducerFactory");
        return new DefaultKafkaProducerFactory<>(configProps);
    }

    @Bean
    public KafkaTemplate<String, Object> kafkaTemplate() {
        log.info("Создание KafkaTemplate");
        return new KafkaTemplate<>(producerFactory());
    }
}
