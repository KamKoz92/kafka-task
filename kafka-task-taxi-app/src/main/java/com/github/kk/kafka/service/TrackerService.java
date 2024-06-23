package com.github.kk.kafka.service;

import com.github.kk.kafka.model.Coords;
import com.github.kk.kafka.model.Signal;
import com.github.kk.kafka.model.Vehicle;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class TrackerService {

    Map<String, Coords> vehicleCurrDistance = new HashMap<>();
    Map<String, Double> vehicleMaxDistance = new HashMap<>();


    @Autowired
    KafkaTemplate<String, Vehicle> kafkaTemplate;

    @KafkaListener(topics = "inputTopic2", groupId = "group1", containerFactory = "kafkaListenerContainerFactorySignal")
    public void processMessage1(@Payload Signal signal) {
        doMath(signal);
    }

    @KafkaListener(topics = "inputTopic2", groupId = "group1", containerFactory = "kafkaListenerContainerFactorySignal")
    public void processMessage2(@Payload Signal signal) {
        doMath(signal);
    }

    @KafkaListener(topics = "inputTopic2", groupId = "group1", containerFactory = "kafkaListenerContainerFactorySignal")
    public void processMessage3(@Payload Signal signal) {
        doMath(signal);
    }

    private void doMath(Signal signal) {
        Coords lastStoredCoords = vehicleCurrDistance.computeIfAbsent(signal.getVehicle(), k -> signal.getCoords());

        double dist = Coords.calculateDistance(signal.getCoords(), lastStoredCoords);

        double maxDist = vehicleMaxDistance.merge(signal.getVehicle(), dist, Double::sum);
        Vehicle v = new Vehicle();
        v.setDist(maxDist);
        v.setName(signal.getVehicle());
        kafkaTemplate.send("outputTopic2", signal.getVehicle(), v);
    }

}
