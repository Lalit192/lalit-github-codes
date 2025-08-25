package com.pm.notificationservice.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "notifications")
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String recipientEmail;

    @Column(nullable = false)
    private String recipientName;

    @Column(nullable = false)
    private String subject;

    @Column(columnDefinition = "TEXT")
    private String message;

    @Enumerated(EnumType.STRING)
    private NotificationType type;

    @Enumerated(EnumType.STRING)
    private NotificationStatus status = NotificationStatus.PENDING;

    private String sourceService;
    private String sourceEntityId;

    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime sentAt;

    // Constructors
    public Notification() {}

    public Notification(String recipientEmail, String recipientName, String subject, 
                       String message, NotificationType type, String sourceService, String sourceEntityId) {
        this.recipientEmail = recipientEmail;
        this.recipientName = recipientName;
        this.subject = subject;
        this.message = message;
        this.type = type;
        this.sourceService = sourceService;
        this.sourceEntityId = sourceEntityId;
    }

    // Getters and Setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public String getRecipientEmail() { return recipientEmail; }
    public void setRecipientEmail(String recipientEmail) { this.recipientEmail = recipientEmail; }

    public String getRecipientName() { return recipientName; }
    public void setRecipientName(String recipientName) { this.recipientName = recipientName; }

    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public NotificationType getType() { return type; }
    public void setType(NotificationType type) { this.type = type; }

    public NotificationStatus getStatus() { return status; }
    public void setStatus(NotificationStatus status) { this.status = status; }

    public String getSourceService() { return sourceService; }
    public void setSourceService(String sourceService) { this.sourceService = sourceService; }

    public String getSourceEntityId() { return sourceEntityId; }
    public void setSourceEntityId(String sourceEntityId) { this.sourceEntityId = sourceEntityId; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getSentAt() { return sentAt; }
    public void setSentAt(LocalDateTime sentAt) { this.sentAt = sentAt; }

    public enum NotificationType {
        PATIENT_WELCOME, APPOINTMENT_CONFIRMATION, BILLING_REMINDER, DOCTOR_ASSIGNMENT, APPOINTMENT_REMINDER
    }

    public enum NotificationStatus {
        PENDING, SENT, FAILED
    }
}