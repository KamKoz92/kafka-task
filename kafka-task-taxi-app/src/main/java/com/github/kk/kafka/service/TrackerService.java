package com.github.kk.kafka.service;

import com.github.kk.kafka.model.Coords;
import com.github.kk.kafka.model.Signal;
import com.github.kk.kafka.model.Vehicle;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
public class TrackerService {

    Map<String, Coords> vehicleCurrDistance = new HashMap<>();
    Map<String, Double> vehicleMaxDistance = new HashMap<>();


    @Autowired
    KafkaTemplate<String, Vehicle> kafkaTemplate;

    @Transactional
    @KafkaListener(topics = "inputTopic2", groupId = "group1", containerFactory = "kafkaListenerContainerFactorySignal")
    public void processMessage1(@Payload Signal signal) {
        doMath(signal);
    }

    @Transactional
    @KafkaListener(topics = "inputTopic2", groupId = "group1", containerFactory = "kafkaListenerContainerFactorySignal")
    public void processMessage2(@Payload Signal signal) {
        doMath(signal);
    }

    @Transactional
    @KafkaListener(topics = "inputTopic2", groupId = "group1", containerFactory = "kafkaListenerContainerFactorySignal")
    public void processMessage3(@Payload Signal signal) {
        doMath(signal);
    }

    private void doMath(Signal signal) {
        log.info("Got signal {}", signal.getVehicle());

        Coords lastStoredCoords = vehicleCurrDistance.computeIfAbsent(signal.getVehicle(), k -> signal.getCoords());
        double dist = Coords.calculateDistance(signal.getCoords(), lastStoredCoords);
        double maxDist = vehicleMaxDistance.merge(signal.getVehicle(), dist, Double::sum);
        Vehicle v = new Vehicle();
        v.setDist(maxDist);
        v.setName(signal.getVehicle());
        CompletableFuture<SendResult<String, Vehicle>> future = kafkaTemplate.send("outputTopic2", signal.getVehicle(), v);

        future.thenAccept(TrackerService::onSuccess)
                .exceptionally(TrackerService::onFailure);
    }

    private void doMathTrans(Signal signal) {
        log.info("Got signal {}", signal.getVehicle());
        Coords lastStoredCoords = vehicleCurrDistance.get(signal.getVehicle());
        double currDist = Coords.calculateDistance(signal.getCoords(), lastStoredCoords);
        double currMaxDist = vehicleMaxDistance.get(signal.getVehicle());
        Vehicle v = new Vehicle();
        v.setDist(currDist);
        v.setName(signal.getVehicle());
        CompletableFuture<SendResult<String, Vehicle>> future = kafkaTemplate.send("outputTopic2", signal.getVehicle(), v);
        future.thenAccept(result -> onSuccessTrans(result, vehicleCurrDistance, vehicleMaxDistance, signal, currMaxDist))
                .exceptionally(TrackerService::onFailure);
        throw new RuntimeException();
    }

    private static Void onFailure(Throwable ex) {
        log.error("Unable to send signal due to {}", ex.getMessage());
        return null;
    }

    private static void onSuccessTrans(SendResult<String, Vehicle> result,
                                  Map<String, Coords> vehicleCurrDistance,
                                  Map<String, Double> vehicleMaxDistance,
                                  Signal currSignal,
                                  double currDistance) {
        vehicleCurrDistance.computeIfAbsent(currSignal.getVehicle(), k -> currSignal.getCoords());
        vehicleMaxDistance.merge(currSignal.getVehicle(), currDistance, Double::sum);
        log.info("Sent vehicle with offset {} ", result.getRecordMetadata().offset());
    }

    private static void onSuccess(SendResult<String, Vehicle> result) {
        log.info("Sent vehicle with offset {} ", result.getRecordMetadata().offset());
    }

}
