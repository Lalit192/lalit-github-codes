package com.pm.patientservice.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pm.patientservice.model.Patient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class PatientEventPublisher {

    private static final Logger log = LoggerFactory.getLogger(PatientEventPublisher.class);
    
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public PatientEventPublisher(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = new ObjectMapper();
    }

    public void publishPatientCreatedEvent(Patient patient) {
        try {
            Map<String, Object> event = new HashMap<>();
            event.put("eventType", "PATIENT_CREATED");
            event.put("timestamp", System.currentTimeMillis());
            
            Map<String, Object> patientData = new HashMap<>();
            patientData.put("id", patient.getId().toString());
            patientData.put("name", patient.getName());
            patientData.put("email", patient.getEmail());
            patientData.put("dateOfBirth", patient.getDateOfBirth().toString());
            patientData.put("address", patient.getAddress());
            
            event.put("data", patientData);
            
            String eventJson = objectMapper.writeValueAsString(event);
            
            kafkaTemplate.send("patient-events", eventJson);
            log.info("📤 Published patient created event for: {}", patient.getName());
            
        } catch (Exception e) {
            log.error("❌ Failed to publish patient event: {}", e.getMessage());
        }
    }
}