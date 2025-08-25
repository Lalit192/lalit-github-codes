package com.pm.doctorservice.controller;

import com.pm.doctorservice.dto.AppointmentResponseDTO;
import com.pm.doctorservice.model.Appointment;
import com.pm.doctorservice.model.Doctor;
import com.pm.doctorservice.repository.AppointmentRepository;
import com.pm.doctorservice.repository.DoctorRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/test-appointments")
public class TestAppointmentController {

    private final AppointmentRepository appointmentRepository;
    private final DoctorRepository doctorRepository;
    private final RestTemplate restTemplate;

    public TestAppointmentController(AppointmentRepository appointmentRepository, 
                                   DoctorRepository doctorRepository, 
                                   RestTemplate restTemplate) {
        this.appointmentRepository = appointmentRepository;
        this.doctorRepository = doctorRepository;
        this.restTemplate = restTemplate;
    }

    @PostMapping("/book")
    public ResponseEntity<AppointmentResponseDTO> bookTestAppointment() {
        try {
            // Get latest patient
            Map<String, Object>[] patients = restTemplate.getForObject("http://java-spring-microservices-patient-service-1:4000/patients", Map[].class);
            if (patients == null || patients.length == 0) {
                return ResponseEntity.badRequest().build();
            }
            Map<String, Object> patient = patients[patients.length - 1];
            UUID patientId = UUID.fromString((String) patient.get("id"));

            // Get latest doctor
            Doctor[] doctors = doctorRepository.findAll().toArray(new Doctor[0]);
            if (doctors.length == 0) {
                return ResponseEntity.badRequest().build();
            }
            Doctor doctor = doctors[doctors.length - 1];

            // Create appointment
            Appointment appointment = new Appointment(
                patientId,
                doctor.getId(),
                LocalDate.of(2025, 1, 25),
                LocalTime.of(10, 30),
                "Regular Checkup",
                "Test appointment - Inter-service communication working!"
            );

            Appointment savedAppointment = appointmentRepository.save(appointment);

            AppointmentResponseDTO response = new AppointmentResponseDTO(
                savedAppointment.getId(),
                savedAppointment.getPatientId(),
                (String) patient.get("name"),
                savedAppointment.getDoctorId(),
                doctor.getName(),
                savedAppointment.getAppointmentDate(),
                savedAppointment.getAppointmentTime(),
                savedAppointment.getReason(),
                savedAppointment.getNotes(),
                savedAppointment.getStatus()
            );

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }
}