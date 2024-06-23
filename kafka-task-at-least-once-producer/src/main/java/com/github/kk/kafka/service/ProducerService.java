package com.github.kk.kafka.service;

import com.github.kk.kafka.model.MessageObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
public class ProducerService {

    KafkaTemplate<String, MessageObject> kafkaTemplate;

    public ProducerService(KafkaTemplate<String, MessageObject> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void send(MessageObject message) {
        log.info("Sending message with text {}", message.getText());
        MessageObject m = new MessageObject();
        CompletableFuture<SendResult<String, MessageObject>> future = kafkaTemplate.send("myTopic", message);
        future.thenAccept(ProducerService::onSuccess)
                .exceptionally(ProducerService::onFailure);
    }

    private static Void onFailure(Throwable ex) {
        log.error("Unable to send message due to  {}", ex.getMessage());
        return null;
    }

    private static void onSuccess(SendResult<String, MessageObject> result) {
        log.info("Sent message with offset {} ", result.getRecordMetadata().offset());
    }
}
