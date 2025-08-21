package ru.practicum;

import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
@Slf4j
public class AggregatorConsumer {
    public final static String BOOTSTRAP_SERVERS = "localhost:9092";
    public final static String KEY_DESERIALIZER = "org.apache.kafka.common.serialization.StringDeserializer";
    public final static String VALUE_DESERIALIZER = "ru.practicum.kafka.deserializer.SensorEventDeserializer";
    public final static String CLIENT_GROUP = "aggregator-group";
    public final static String SENSOR_TOPIC = "telemetry.sensors.v1";


    @Bean
    public KafkaConsumer<String, SpecificRecordBase> createConsumer() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
        configProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, KEY_DESERIALIZER);
        configProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, VALUE_DESERIALIZER);
        configProps.put(ConsumerConfig.GROUP_ID_CONFIG, CLIENT_GROUP);

        log.info("Создание консьюмера для аггрегатора");
        return new KafkaConsumer<>(configProps);
    }
}
