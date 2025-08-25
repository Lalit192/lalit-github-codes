package com.pm.appointmentservice.controller;

import com.pm.appointmentservice.dto.AppointmentRequestDTO;
import com.pm.appointmentservice.model.Appointment;
import com.pm.appointmentservice.service.AppointmentService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/appointments")
@CrossOrigin(origins = "*")
public class AppointmentController {

    private final AppointmentService appointmentService;

    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @PostMapping
    public Mono<ResponseEntity<Appointment>> createAppointment(@Valid @RequestBody AppointmentRequestDTO request) {
        return appointmentService.createAppointment(request)
                .map(appointment -> ResponseEntity.ok(appointment))
                .onErrorReturn(ResponseEntity.badRequest().build());
    }

    @GetMapping
    public ResponseEntity<List<Appointment>> getAllAppointments() {
        List<Appointment> appointments = appointmentService.getAllAppointments();
        return ResponseEntity.ok(appointments);
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<Appointment>> getAppointmentsByPatient(@PathVariable String patientId) {
        List<Appointment> appointments = appointmentService.getAppointmentsByPatient(patientId);
        return ResponseEntity.ok(appointments);
    }

    @GetMapping("/doctor/{doctorId}")
    public ResponseEntity<List<Appointment>> getAppointmentsByDoctor(@PathVariable String doctorId) {
        List<Appointment> appointments = appointmentService.getAppointmentsByDoctor(doctorId);
        return ResponseEntity.ok(appointments);
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<Appointment> updateAppointmentStatus(
            @PathVariable UUID id,
            @RequestParam Appointment.AppointmentStatus status) {
        
        try {
            Appointment updatedAppointment = appointmentService.updateAppointmentStatus(id, status);
            return ResponseEntity.ok(updatedAppointment);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getAppointmentStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalAppointments", appointmentService.getAppointmentStats("total"));
        stats.put("scheduledAppointments", appointmentService.getAppointmentStats("scheduled"));
        stats.put("completedAppointments", appointmentService.getAppointmentStats("completed"));
        stats.put("cancelledAppointments", appointmentService.getAppointmentStats("cancelled"));
        return ResponseEntity.ok(stats);
    }
}