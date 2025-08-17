package ru.practicum;

import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

import static ru.practicum.AggregatorConsumer.BOOTSTRAP_SERVERS;

@Component
public class AggregatorProducer {
    public final static String KEY_SERIALIZER = "org.apache.kafka.common.serialization.StringSerializer";
    public final static String VALUE_SERIALIZER = "ru.practicum.kafka.serializer.AvroSerializer";
    public final static String SNAPSHOT_TOPIC = "telemetry.snapshots.v1";

    @Bean
    public KafkaProducer<String, SpecificRecordBase> createProducer() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, KEY_SERIALIZER);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, VALUE_SERIALIZER);

        return new KafkaProducer<>(configProps);
    }
}
