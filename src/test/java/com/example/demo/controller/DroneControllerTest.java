package com.example.demo.controller;

import com.example.demo.dto.DroneMessage;
import com.example.demo.service.KafkaProducerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(DroneController.class)
class DroneControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean //Баг idea, все работает!
    private KafkaProducerService kafkaProducerService;

    @Test
    void testCreateDroneMessage_Success() throws Exception {
        DroneMessage droneMessage = createValidDroneMessage();

        mockMvc.perform(post("/api/v1/drones/message")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(droneMessage)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.message").value("Drone message sent to Kafka successfully"));

        Mockito.verify(kafkaProducerService).sendMessage(Mockito.any());
    }

    @Test
    void testCreateDroneMessage_ValidationFailed() throws Exception {
        DroneMessage invalidDroneMessage = createInvalidDroneMessage();

        mockMvc.perform(post("/api/v1/drones/message")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDroneMessage)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("error"))
                .andExpect(jsonPath("$.errors.model").exists())
                .andExpect(jsonPath("$.errors.speed").exists());
    }

    private DroneMessage createValidDroneMessage() {
        DroneMessage droneMessage = new DroneMessage();
        droneMessage.setModel("DJI Mavic 3");
        droneMessage.setSpeed(15.5);
        droneMessage.setLatitude(55.7558);
        droneMessage.setLongitude(37.6173);
        droneMessage.setAltitude(120.0);
        droneMessage.setDetectedBy("Radar Station Alpha");
        return droneMessage;
    }

    private DroneMessage createInvalidDroneMessage() {
        DroneMessage droneMessage = new DroneMessage();
        droneMessage.setModel("");
        droneMessage.setSpeed(-5.0);
        droneMessage.setLatitude(55.7558);
        droneMessage.setLongitude(37.6173);
        droneMessage.setAltitude(100.0);
        droneMessage.setDetectedBy("Radar");
        return droneMessage;
    }

}