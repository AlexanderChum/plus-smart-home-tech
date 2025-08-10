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
    public final static String BOOTSTRAPSERVERS = "localhost:9092";
    public final static String KEYSERIALIZER = "org.apache.kafka.common.serialization.StringSerializer";
    public final static String VALUESERIALIZER = "ru.practicum.kafka.serializer.AvroSerializer";
    public final static String SENSORTOPIC = "telemetry.sensors.v1";
    public final static String HUBTOPIC = "telemetry.hubs.v1";

    @Bean
    public ProducerFactory<String, Object> producerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAPSERVERS);
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, KEYSERIALIZER);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, VALUESERIALIZER);

        log.info("Создание ProducerFactory");
        return new DefaultKafkaProducerFactory<>(configProps);
    }

    @Bean
    public KafkaTemplate<String, Object> kafkaTemplate() {
        log.info("Создание KafkaTemplate");
        return new KafkaTemplate<>(producerFactory());
    }
}
