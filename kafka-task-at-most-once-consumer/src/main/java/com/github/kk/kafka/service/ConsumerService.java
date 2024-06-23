package com.github.kk.kafka.service;

import com.github.kk.kafka.model.MessageObject;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
public class ConsumerService {

    // Auto acknowledgment 100 ms interval
    @KafkaListener(topics = "myTopic")
    public void processMessage(@Payload MessageObject message) {
        System.out.println("Received: " + message.getText());
    }

    // Manual acknowledgment
    //    @KafkaListener(topics = "myTopic")
    //    public void processMessage(@Payload String content, Acknowledgment ack) {
    //        ack.acknowledge();
    //        System.out.println("Received: " + content);
    //    }
}
