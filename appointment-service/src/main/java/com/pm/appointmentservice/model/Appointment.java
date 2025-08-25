package com.pm.appointmentservice.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "appointments")
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String patientId;

    @Column(nullable = false)
    private String patientName;

    @Column(nullable = false)
    private String patientEmail;

    @Column(nullable = false)
    private String doctorId;

    @Column(nullable = false)
    private String doctorName;

    @Column(nullable = false)
    private LocalDateTime appointmentDateTime;

    @Enumerated(EnumType.STRING)
    private AppointmentStatus status = AppointmentStatus.SCHEDULED;

    @Enumerated(EnumType.STRING)
    private AppointmentType type;

    private String notes;
    private String billingAccountId;

    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime updatedAt = LocalDateTime.now();

    // Constructors
    public Appointment() {}

    public Appointment(String patientId, String patientName, String patientEmail,
                      String doctorId, String doctorName, LocalDateTime appointmentDateTime,
                      AppointmentType type, String notes) {
        this.patientId = patientId;
        this.patientName = patientName;
        this.patientEmail = patientEmail;
        this.doctorId = doctorId;
        this.doctorName = doctorName;
        this.appointmentDateTime = appointmentDateTime;
        this.type = type;
        this.notes = notes;
    }

    // Getters and Setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public String getPatientId() { return patientId; }
    public void setPatientId(String patientId) { this.patientId = patientId; }

    public String getPatientName() { return patientName; }
    public void setPatientName(String patientName) { this.patientName = patientName; }

    public String getPatientEmail() { return patientEmail; }
    public void setPatientEmail(String patientEmail) { this.patientEmail = patientEmail; }

    public String getDoctorId() { return doctorId; }
    public void setDoctorId(String doctorId) { this.doctorId = doctorId; }

    public String getDoctorName() { return doctorName; }
    public void setDoctorName(String doctorName) { this.doctorName = doctorName; }

    public LocalDateTime getAppointmentDateTime() { return appointmentDateTime; }
    public void setAppointmentDateTime(LocalDateTime appointmentDateTime) { this.appointmentDateTime = appointmentDateTime; }

    public AppointmentStatus getStatus() { return status; }
    public void setStatus(AppointmentStatus status) { this.status = status; }

    public AppointmentType getType() { return type; }
    public void setType(AppointmentType type) { this.type = type; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public String getBillingAccountId() { return billingAccountId; }
    public void setBillingAccountId(String billingAccountId) { this.billingAccountId = billingAccountId; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public enum AppointmentStatus {
        SCHEDULED, CONFIRMED, IN_PROGRESS, COMPLETED, CANCELLED, NO_SHOW
    }

    public enum AppointmentType {
        CONSULTATION, FOLLOW_UP, EMERGENCY, SURGERY, CHECKUP
    }
}