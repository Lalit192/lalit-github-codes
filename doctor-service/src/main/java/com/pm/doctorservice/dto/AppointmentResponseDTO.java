package com.pm.doctorservice.dto;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

public class AppointmentResponseDTO {

    private UUID id;
    private UUID patientId;
    private String patientName;
    private UUID doctorId;
    private String doctorName;
    private LocalDate appointmentDate;
    private LocalTime appointmentTime;
    private String reason;
    private String notes;
    private String status;

    // Constructors
    public AppointmentResponseDTO() {}

    public AppointmentResponseDTO(UUID id, UUID patientId, String patientName, UUID doctorId, 
                                String doctorName, LocalDate appointmentDate, LocalTime appointmentTime, 
                                String reason, String notes, String status) {
        this.id = id;
        this.patientId = patientId;
        this.patientName = patientName;
        this.doctorId = doctorId;
        this.doctorName = doctorName;
        this.appointmentDate = appointmentDate;
        this.appointmentTime = appointmentTime;
        this.reason = reason;
        this.notes = notes;
        this.status = status;
    }

    // Getters and Setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public UUID getPatientId() { return patientId; }
    public void setPatientId(UUID patientId) { this.patientId = patientId; }

    public String getPatientName() { return patientName; }
    public void setPatientName(String patientName) { this.patientName = patientName; }

    public UUID getDoctorId() { return doctorId; }
    public void setDoctorId(UUID doctorId) { this.doctorId = doctorId; }

    public String getDoctorName() { return doctorName; }
    public void setDoctorName(String doctorName) { this.doctorName = doctorName; }

    public LocalDate getAppointmentDate() { return appointmentDate; }
    public void setAppointmentDate(LocalDate appointmentDate) { this.appointmentDate = appointmentDate; }

    public LocalTime getAppointmentTime() { return appointmentTime; }
    public void setAppointmentTime(LocalTime appointmentTime) { this.appointmentTime = appointmentTime; }

    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}