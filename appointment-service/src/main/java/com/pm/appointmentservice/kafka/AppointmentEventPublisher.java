package com.pm.appointmentservice.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pm.appointmentservice.model.Appointment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class AppointmentEventPublisher {

    private static final Logger log = LoggerFactory.getLogger(AppointmentEventPublisher.class);
    
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public AppointmentEventPublisher(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = new ObjectMapper();
    }

    public void publishAppointmentCreatedEvent(Appointment appointment) {
        try {
            Map<String, Object> event = new HashMap<>();
            event.put("eventType", "APPOINTMENT_CREATED");
            event.put("timestamp", System.currentTimeMillis());
            
            Map<String, Object> appointmentData = new HashMap<>();
            appointmentData.put("id", appointment.getId().toString());
            appointmentData.put("patientId", appointment.getPatientId());
            appointmentData.put("patientName", appointment.getPatientName());
            appointmentData.put("patientEmail", appointment.getPatientEmail());
            appointmentData.put("doctorId", appointment.getDoctorId());
            appointmentData.put("doctorName", appointment.getDoctorName());
            appointmentData.put("appointmentDateTime", appointment.getAppointmentDateTime().toString());
            appointmentData.put("type", appointment.getType().toString());
            appointmentData.put("status", appointment.getStatus().toString());
            
            event.put("data", appointmentData);
            
            String eventJson = objectMapper.writeValueAsString(event);
            
            kafkaTemplate.send("appointment-events", eventJson);
            log.info("📤 Published appointment created event for: {}", appointment.getPatientName());
            
        } catch (Exception e) {
            log.error("❌ Failed to publish appointment event: {}", e.getMessage());
        }
    }

    public void publishAppointmentStatusUpdatedEvent(Appointment appointment) {
        try {
            Map<String, Object> event = new HashMap<>();
            event.put("eventType", "APPOINTMENT_STATUS_UPDATED");
            event.put("timestamp", System.currentTimeMillis());
            
            Map<String, Object> appointmentData = new HashMap<>();
            appointmentData.put("id", appointment.getId().toString());
            appointmentData.put("patientName", appointment.getPatientName());
            appointmentData.put("patientEmail", appointment.getPatientEmail());
            appointmentData.put("doctorName", appointment.getDoctorName());
            appointmentData.put("status", appointment.getStatus().toString());
            appointmentData.put("appointmentDateTime", appointment.getAppointmentDateTime().toString());
            
            event.put("data", appointmentData);
            
            String eventJson = objectMapper.writeValueAsString(event);
            
            kafkaTemplate.send("appointment-events", eventJson);
            log.info("📤 Published appointment status update event: {}", appointment.getStatus());
            
        } catch (Exception e) {
            log.error("❌ Failed to publish appointment status event: {}", e.getMessage());
        }
    }
}