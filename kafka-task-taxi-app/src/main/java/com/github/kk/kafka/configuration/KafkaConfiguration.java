package com.github.kk.kafka.configuration;

import com.github.kk.kafka.model.Signal;
import com.github.kk.kafka.model.Vehicle;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.*;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConfiguration {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Bean
    public KafkaTemplate<String, Signal> kafkaTemplateSignal() {
        return new KafkaTemplate<>(producerFactorySignal());
    }

    @Bean
    public KafkaTemplate<String, Vehicle> kafkaTemplateVehicle() {
        return new KafkaTemplate<>(producerFactoryVehicle());
    }

    @Bean
    public ProducerFactory<String, Signal> producerFactorySignal() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        configProps.put(ProducerConfig.ACKS_CONFIG, "all");
        configProps.put(ProducerConfig.RETRIES_CONFIG, 10);

        return new DefaultKafkaProducerFactory<>(configProps);
    }

    @Bean
    public ProducerFactory<String, Vehicle> producerFactoryVehicle() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        configProps.put(ProducerConfig.ACKS_CONFIG, "all");
        configProps.put(ProducerConfig.RETRIES_CONFIG, 10);

        return new DefaultKafkaProducerFactory<>(configProps);
    }

    @Bean
    public Map<String, Object> consumerConfigs() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, true);
        props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, 100);

        return props;
    }

    @Bean
    public ConsumerFactory<String, Signal> consumerFactorySignal() {
        JsonDeserializer<Signal> deserializer = new JsonDeserializer<>(Signal.class, false);
        deserializer.addTrustedPackages("com.github.kk.kafka.model");
        return new DefaultKafkaConsumerFactory<>(consumerConfigs(), new StringDeserializer(), deserializer);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, Signal> kafkaListenerContainerFactorySignal() {
        ConcurrentKafkaListenerContainerFactory<String, Signal> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactorySignal());
        return factory;
    }

    @Bean
    public ConsumerFactory<String, Vehicle> consumerFactoryVehicle() {
        JsonDeserializer<Vehicle> deserializer = new JsonDeserializer<>(Vehicle.class, false);
        deserializer.addTrustedPackages("com.github.kk.kafka.model");
        return new DefaultKafkaConsumerFactory<>(consumerConfigs(), new StringDeserializer(), deserializer);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, Vehicle> kafkaListenerContainerFactoryVehicle() {
        ConcurrentKafkaListenerContainerFactory<String, Vehicle> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactoryVehicle());
        return factory;
    }

    @Bean
    public NewTopic topic1() {
        return TopicBuilder.name("inputTopic2")
                .partitions(3)
                .replicas(2)
                .build();
    }

    @Bean
    public NewTopic topic2() {
        return TopicBuilder.name("outputTopic2")
                .partitions(3)
                .replicas(2)
                .build();
    }
}