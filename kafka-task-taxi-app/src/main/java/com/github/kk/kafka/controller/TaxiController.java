package com.github.kk.kafka.controller;


import com.github.kk.kafka.model.Signal;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;

@Slf4j
@RestController
public class TaxiController {

    private final KafkaTemplate<String, Signal> kafkaTemplate;

    public TaxiController(KafkaTemplate<String, Signal> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @PostMapping("/publish")
    public void send(@RequestBody Signal signal) {
        if (StringUtils.isBlank(signal.getId()) || Objects.isNull(signal.getCoords())) {
            throw new IllegalArgumentException("Missing signal parameters");
        }

        CompletableFuture<SendResult<String, Signal>> future = kafkaTemplate.send("inputTopic2", signal.getVehicle(),  signal);
        future.thenAccept(TaxiController::onSuccess)
                .exceptionally(TaxiController::onFailure);
    }

    private static Void onFailure(Throwable ex) {
        log.error("Unable to send signal due to {}", ex.getMessage());
        return null;
    }

    private static void onSuccess(SendResult<String, Signal> result) {
        log.info("Sent signal with offset {} ", result.getRecordMetadata().offset());
    }
}