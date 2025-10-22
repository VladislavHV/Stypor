package com.example.demo.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducerService {

    private static final Logger log = LoggerFactory.getLogger(KafkaProducerService.class);
    private static final String TOPIC = "drone-messages";

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    public void sendMessage(Object message) {
        try {
            kafkaTemplate.send(TOPIC, message);
            log.info("Message sent to Kafka topic '{}': {}", TOPIC, message);
        } catch (Exception e) {
            log.error("Error sending message to Kafka: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to send message to Kafka", e);
        }
    }

}
