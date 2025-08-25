package com.pm.doctorservice.controller;

import com.pm.doctorservice.dto.AppointmentRequestDTO;
import com.pm.doctorservice.dto.AppointmentResponseDTO;
import com.pm.doctorservice.service.AppointmentService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/appointments")
public class AppointmentController {

    private final AppointmentService appointmentService;

    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @Operation(summary = "Book appointment - communicates with Patient Service")
    @PostMapping
    public ResponseEntity<AppointmentResponseDTO> bookAppointment(@Valid @RequestBody AppointmentRequestDTO requestDTO) {
        try {
            AppointmentResponseDTO appointment = appointmentService.bookAppointment(requestDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(appointment);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(summary = "Get appointments by doctor ID")
    @GetMapping("/doctor/{doctorId}")
    public ResponseEntity<List<AppointmentResponseDTO>> getAppointmentsByDoctor(@PathVariable UUID doctorId) {
        List<AppointmentResponseDTO> appointments = appointmentService.getAppointmentsByDoctor(doctorId);
        return ResponseEntity.ok(appointments);
    }

    @Operation(summary = "Get appointments by patient ID - calls Patient Service")
    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<AppointmentResponseDTO>> getAppointmentsByPatient(@PathVariable UUID patientId) {
        List<AppointmentResponseDTO> appointments = appointmentService.getAppointmentsByPatient(patientId);
        return ResponseEntity.ok(appointments);
    }

    @Operation(summary = "Get patient details with appointments - inter-service communication")
    @GetMapping("/patient-details/{patientId}")
    public ResponseEntity<?> getPatientWithAppointments(@PathVariable UUID patientId) {
        try {
            Object patientDetails = appointmentService.getPatientDetailsWithAppointments(patientId);
            return ResponseEntity.ok(patientDetails);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}