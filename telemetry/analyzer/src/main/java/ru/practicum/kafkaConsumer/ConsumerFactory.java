package ru.practicum.kafkaConsumer;

import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class ConsumerFactory {
    public final static String BOOTSTRAP_SERVERS = "localhost:9092";
    public final static String KEY_DESERIALIZER = "org.apache.kafka.common.serialization.StringDeserializer";

    public final static String VALUE_HUB_DESERIALIZER = "ru.practicum.kafka.deserializer.HubEventAvroDeserializer";
    public final static String VALUE_SNAPSHOT_DESERIALIZER = "ru.practicum.kafka.deserializer.SensorEventDeserializer";

    public final static String CLIENT_HUB_GROUP = "analyzer-hub-group";
    public final static String CLIENT_SNAPSHOT_GROUP = "analyzer-snapshot-group";

    public final static String HUB_TOPIC = "telemetry.sensors.v1";
    public final static String SNAPSHOT_TOPIC = "telemetry.snapshots.v1";

    @Bean
    public KafkaConsumer<String, SpecificRecordBase> createSnapshotConsumer() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
        configProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, KEY_DESERIALIZER);
        configProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, VALUE_SNAPSHOT_DESERIALIZER);
        configProps.put(ConsumerConfig.GROUP_ID_CONFIG, CLIENT_SNAPSHOT_GROUP);

        return new KafkaConsumer<>(configProps);
    }

    @Bean
    public KafkaConsumer<String, SpecificRecordBase> createHubConsumer() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
        configProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, KEY_DESERIALIZER);
        configProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, VALUE_HUB_DESERIALIZER);
        configProps.put(ConsumerConfig.GROUP_ID_CONFIG, CLIENT_HUB_GROUP);

        return new KafkaConsumer<>(configProps);
    }
}
