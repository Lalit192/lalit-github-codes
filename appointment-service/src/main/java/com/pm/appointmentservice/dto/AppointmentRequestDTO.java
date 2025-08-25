package com.pm.appointmentservice.dto;

import com.pm.appointmentservice.model.Appointment;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public class AppointmentRequestDTO {

    @NotBlank(message = "Patient ID is required")
    private String patientId;

    @NotBlank(message = "Doctor ID is required")
    private String doctorId;

    @NotNull(message = "Appointment date and time is required")
    private LocalDateTime appointmentDateTime;

    @NotNull(message = "Appointment type is required")
    private Appointment.AppointmentType type;

    private String notes;

    // Constructors
    public AppointmentRequestDTO() {}

    public AppointmentRequestDTO(String patientId, String doctorId, LocalDateTime appointmentDateTime,
                                Appointment.AppointmentType type, String notes) {
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.appointmentDateTime = appointmentDateTime;
        this.type = type;
        this.notes = notes;
    }

    // Getters and Setters
    public String getPatientId() { return patientId; }
    public void setPatientId(String patientId) { this.patientId = patientId; }

    public String getDoctorId() { return doctorId; }
    public void setDoctorId(String doctorId) { this.doctorId = doctorId; }

    public LocalDateTime getAppointmentDateTime() { return appointmentDateTime; }
    public void setAppointmentDateTime(LocalDateTime appointmentDateTime) { this.appointmentDateTime = appointmentDateTime; }

    public Appointment.AppointmentType getType() { return type; }
    public void setType(Appointment.AppointmentType type) { this.type = type; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
}