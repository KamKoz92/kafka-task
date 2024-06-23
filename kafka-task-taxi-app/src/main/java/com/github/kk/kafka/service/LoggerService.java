package com.github.kk.kafka.service;

import com.github.kk.kafka.model.Vehicle;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Slf4j
@Service
class LoggerService {

    @KafkaListener(topics = "outputTopic2", groupId = "group2", containerFactory = "kafkaListenerContainerFactoryVehicle")
    public void processMessage1(@Payload Vehicle vehicle) {
        log.info("Vehicle {} traveled {} distance", vehicle.getName(), vehicle.getDist());
    }
}
