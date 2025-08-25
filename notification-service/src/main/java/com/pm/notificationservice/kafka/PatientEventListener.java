package com.pm.notificationservice.kafka;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pm.notificationservice.service.NotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class PatientEventListener {

    private static final Logger log = LoggerFactory.getLogger(PatientEventListener.class);
    
    private final NotificationService notificationService;
    private final ObjectMapper objectMapper;

    public PatientEventListener(NotificationService notificationService) {
        this.notificationService = notificationService;
        this.objectMapper = new ObjectMapper();
    }

    @KafkaListener(topics = "patient-events", groupId = "notification-service")
    public void handlePatientEvent(String message) {
        try {
            log.info("📨 Received patient event: {}", message);
            
            JsonNode eventData = objectMapper.readTree(message);
            String eventType = eventData.get("eventType").asText();
            
            if ("PATIENT_CREATED".equals(eventType)) {
                JsonNode patientData = eventData.get("data");
                String patientId = patientData.get("id").asText();
                String patientName = patientData.get("name").asText();
                String patientEmail = patientData.get("email").asText();
                
                // Send welcome email
                notificationService.sendWelcomeEmail(patientEmail, patientName, patientId);
                log.info("✅ Welcome email sent to patient: {}", patientName);
            }
            
        } catch (Exception e) {
            log.error("❌ Error processing patient event: {}", e.getMessage());
        }
    }

    @KafkaListener(topics = "billing-events", groupId = "notification-service")
    public void handleBillingEvent(String message) {
        try {
            log.info("📨 Received billing event: {}", message);
            
            JsonNode eventData = objectMapper.readTree(message);
            String eventType = eventData.get("eventType").asText();
            
            if ("BILLING_ACCOUNT_CREATED".equals(eventType)) {
                JsonNode billingData = eventData.get("data");
                String accountNumber = billingData.get("accountNumber").asText();
                String patientName = billingData.get("patientName").asText();
                String patientEmail = billingData.get("email").asText();
                
                // Send billing confirmation email
                notificationService.sendBillingReminder(patientEmail, patientName, accountNumber);
                log.info("✅ Billing confirmation sent to patient: {}", patientName);
            }
            
        } catch (Exception e) {
            log.error("❌ Error processing billing event: {}", e.getMessage());
        }
    }

    @KafkaListener(topics = "appointment-events", groupId = "notification-service")
    public void handleAppointmentEvent(String message) {
        try {
            log.info("📨 Received appointment event: {}", message);
            
            JsonNode eventData = objectMapper.readTree(message);
            String eventType = eventData.get("eventType").asText();
            
            if ("APPOINTMENT_CREATED".equals(eventType)) {
                JsonNode appointmentData = eventData.get("data");
                String patientName = appointmentData.get("patientName").asText();
                String patientEmail = appointmentData.get("patientEmail").asText();
                String doctorName = appointmentData.get("doctorName").asText();
                String appointmentDateTime = appointmentData.get("appointmentDateTime").asText();
                String appointmentId = appointmentData.get("id").asText();
                
                // Send appointment confirmation email
                notificationService.sendAppointmentConfirmation(patientEmail, patientName, doctorName, appointmentDateTime, appointmentId);
                log.info("✅ Appointment confirmation sent to patient: {}", patientName);
            }
            
        } catch (Exception e) {
            log.error("❌ Error processing appointment event: {}", e.getMessage());
        }
    }
}