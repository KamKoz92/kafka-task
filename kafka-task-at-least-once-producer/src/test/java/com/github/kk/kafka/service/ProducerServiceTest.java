//package com.github.kk.kafka.service;
//
//import com.github.kk.kafka.model.MessageObject;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.testcontainers.containers.KafkaContainer;
//import org.testcontainers.utility.DockerImageName;
//
//@SpringBootTest
//class ProducerServiceTest {
//
//    @Autowired
//    private ProducerService producerService;
//
//    private KafkaContainer kafka = new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:7.4.4")).withKraft();
//
//    @Test
//    void testSendMessage() {
//        producerService.send(new MessageObject("id", "text"));
//    }
//}