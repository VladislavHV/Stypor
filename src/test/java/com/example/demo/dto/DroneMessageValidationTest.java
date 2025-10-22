package com.example.demo.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class DroneMessageValidationTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testValidDroneMessage() {
        DroneMessage droneMessage = createValidDroneMessage();

        Set<ConstraintViolation<DroneMessage>> violations = validator.validate(droneMessage);

        assertTrue(violations.isEmpty(), "Should have no violations for valid message");
    }

    @Test
    void testInvalidDroneMessage_EmptyModel() {
        DroneMessage droneMessage = createValidDroneMessage();
        droneMessage.setModel("");

        Set<ConstraintViolation<DroneMessage>> violations = validator.validate(droneMessage);

        assertFalse(violations.isEmpty(), "Should have violations for empty model");
        assertTrue(violations.stream()
                        .anyMatch(v -> v.getPropertyPath().toString().equals("model")),
                "Should have model validation error");
    }

    @Test
    void testInvalidDroneMessage_NegativeSpeed() {
        DroneMessage droneMessage = createValidDroneMessage();
        droneMessage.setSpeed(-5.0);

        Set<ConstraintViolation<DroneMessage>> violations = validator.validate(droneMessage);

        assertFalse(violations.isEmpty(), "Should have violations for negative speed");
        assertTrue(violations.stream()
                        .anyMatch(v -> v.getPropertyPath().toString().equals("speed")),
                "Should have speed validation error");
    }

    @Test
    void testInvalidDroneMessage_InvalidLatitude() {
        DroneMessage droneMessage = createValidDroneMessage();
        droneMessage.setLatitude(200.0);

        Set<ConstraintViolation<DroneMessage>> violations = validator.validate(droneMessage);

        assertFalse(violations.isEmpty(), "Should have violations for invalid latitude");
        assertTrue(violations.stream()
                        .anyMatch(v -> v.getPropertyPath().toString().equals("latitude")),
                "Should have latitude validation error");
    }

    @Test
    void testInvalidDroneMessage_InvalidLongitude() {
        DroneMessage droneMessage = createValidDroneMessage();
        droneMessage.setLongitude(-200.0);

        Set<ConstraintViolation<DroneMessage>> violations = validator.validate(droneMessage);

        assertFalse(violations.isEmpty(), "Should have violations for invalid longitude");
        assertTrue(violations.stream()
                        .anyMatch(v -> v.getPropertyPath().toString().equals("longitude")),
                "Should have longitude validation error");
    }

    @Test
    void testInvalidDroneMessage_NegativeAltitude() {
        DroneMessage droneMessage = createValidDroneMessage();
        droneMessage.setAltitude(-10.0);

        Set<ConstraintViolation<DroneMessage>> violations = validator.validate(droneMessage);

        assertFalse(violations.isEmpty(), "Should have violations for negative altitude");
        assertTrue(violations.stream()
                        .anyMatch(v -> v.getPropertyPath().toString().equals("altitude")),
                "Should have altitude validation error");
    }

    @Test
    void testInvalidDroneMessage_EmptyDetectedBy() {
        DroneMessage droneMessage = createValidDroneMessage();
        droneMessage.setDetectedBy("");

        Set<ConstraintViolation<DroneMessage>> violations = validator.validate(droneMessage);

        assertFalse(violations.isEmpty(), "Should have violations for empty detectedBy");
        assertTrue(violations.stream()
                        .anyMatch(v -> v.getPropertyPath().toString().equals("detectedBy")),
                "Should have detectedBy validation error");
    }

    @Test
    void testInvalidDroneMessage_NullFields() {
        DroneMessage droneMessage = new DroneMessage();
        droneMessage.setModel(null);
        droneMessage.setSpeed(null);
        droneMessage.setLatitude(null);
        droneMessage.setLongitude(null);
        droneMessage.setAltitude(null);
        droneMessage.setDetectedBy(null);

        Set<ConstraintViolation<DroneMessage>> violations = validator.validate(droneMessage);

        assertFalse(violations.isEmpty(), "Should have violations for null fields");
        assertEquals(6, violations.size(), "Should have 6 validation errors for null fields");
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
}