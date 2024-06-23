package com.github.kk.kafka.controller;


import com.github.kk.kafka.model.MessageObject;
import com.github.kk.kafka.service.ProducerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class ProducerController {

    private final ProducerService producerService;

    public ProducerController(ProducerService producerService) {
        this.producerService = producerService;
    }

    @PostMapping("/publish")
    public void send(@RequestBody MessageObject message) {
        producerService.send(message);
    }

}