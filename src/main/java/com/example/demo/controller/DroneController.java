package com.example.demo.controller;

import com.example.demo.dto.DroneMessage;
import com.example.demo.service.KafkaProducerService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/drones")
@Validated
public class DroneController {

    private static final Logger log = LoggerFactory.getLogger(DroneController.class);

    @Autowired
    private KafkaProducerService kafkaProducerService;

    @PostMapping("/message")
    public ResponseEntity<Map<String, String>> createDroneMessage(
            @Valid @RequestBody DroneMessage droneMessage) {

        try {
            log.info("Received drone message: {}", droneMessage);

            kafkaProducerService.sendMessage(droneMessage);

            Map<String, String> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "Drone message sent to Kafka successfully");

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("Error processing drone message: {}", e.getMessage(), e);

            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("status", "error");
            errorResponse.put("message", "Failed to process drone message: " + e.getMessage());

            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }

}
