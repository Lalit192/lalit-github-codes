package com.pm.doctorservice.service;

import com.pm.doctorservice.dto.AppointmentRequestDTO;
import com.pm.doctorservice.dto.AppointmentResponseDTO;
import com.pm.doctorservice.model.Appointment;
import com.pm.doctorservice.model.Doctor;
import com.pm.doctorservice.repository.AppointmentRepository;
import com.pm.doctorservice.repository.DoctorRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AppointmentService {

    private static final Logger log = LoggerFactory.getLogger(AppointmentService.class);
    private final AppointmentRepository appointmentRepository;
    private final DoctorRepository doctorRepository;
    private final RestTemplate restTemplate;

    public AppointmentService(AppointmentRepository appointmentRepository, 
                            DoctorRepository doctorRepository, 
                            RestTemplate restTemplate) {
        this.appointmentRepository = appointmentRepository;
        this.doctorRepository = doctorRepository;
        this.restTemplate = restTemplate;
    }

    public AppointmentResponseDTO bookAppointment(AppointmentRequestDTO requestDTO) {
        log.info("Booking appointment - Patient ID: {}, Doctor ID: {}", 
                requestDTO.getPatientId(), requestDTO.getDoctorId());
        
        // Verify doctor exists
        Doctor doctor = doctorRepository.findById(requestDTO.getDoctorId())
                .orElseThrow(() -> {
                    log.error("Doctor not found: {}", requestDTO.getDoctorId());
                    return new RuntimeException("Doctor not found");
                });

        // Call Patient Service to verify patient exists
        String patientServiceUrl = "http://java-spring-microservices-patient-service-1:4000/patients/" + requestDTO.getPatientId();
        log.info("Calling Patient Service: {}", patientServiceUrl);
        
        Map<String, Object> patientData;
        try {
            patientData = restTemplate.getForObject(patientServiceUrl, Map.class);
            if (patientData == null) {
                log.error("Patient Service returned null for ID: {}", requestDTO.getPatientId());
                throw new RuntimeException("Patient not found");
            }
            log.info("Patient found: {}", patientData.get("name"));
        } catch (Exception e) {
            log.error("Failed to call Patient Service: {}", e.getMessage());
            throw new RuntimeException("Patient not found or Patient Service unavailable: " + e.getMessage());
        }

        // Create appointment
        Appointment appointment = new Appointment(
            requestDTO.getPatientId(),
            requestDTO.getDoctorId(),
            LocalDate.parse(requestDTO.getAppointmentDate()),
            LocalTime.parse(requestDTO.getAppointmentTime()),
            requestDTO.getReason(),
            requestDTO.getNotes()
        );

        Appointment savedAppointment = appointmentRepository.save(appointment);

        return mapToResponseDTO(savedAppointment, (String) patientData.get("name"), doctor.getName());
    }

    public List<AppointmentResponseDTO> getAppointmentsByDoctor(UUID doctorId) {
        List<Appointment> appointments = appointmentRepository.findByDoctorId(doctorId);
        return appointments.stream()
                .map(appointment -> {
                    Doctor doctor = doctorRepository.findById(appointment.getDoctorId()).orElse(null);
                    String doctorName = doctor != null ? doctor.getName() : "Unknown";
                    
                    // Get patient name from Patient Service
                    String patientName = getPatientName(appointment.getPatientId());
                    
                    return mapToResponseDTO(appointment, patientName, doctorName);
                })
                .collect(Collectors.toList());
    }

    public List<AppointmentResponseDTO> getAppointmentsByPatient(UUID patientId) {
        List<Appointment> appointments = appointmentRepository.findByPatientId(patientId);
        return appointments.stream()
                .map(appointment -> {
                    Doctor doctor = doctorRepository.findById(appointment.getDoctorId()).orElse(null);
                    String doctorName = doctor != null ? doctor.getName() : "Unknown";
                    
                    // Get patient name from Patient Service
                    String patientName = getPatientName(appointment.getPatientId());
                    
                    return mapToResponseDTO(appointment, patientName, doctorName);
                })
                .collect(Collectors.toList());
    }

    public Object getPatientDetailsWithAppointments(UUID patientId) {
        // Get patient details from Patient Service
        String patientServiceUrl = "http://java-spring-microservices-patient-service-1:4000/patients/" + patientId;
        Map<String, Object> patientData;
        try {
            patientData = restTemplate.getForObject(patientServiceUrl, Map.class);
            if (patientData == null) {
                throw new RuntimeException("Patient not found");
            }
        } catch (Exception e) {
            throw new RuntimeException("Patient not found or Patient Service unavailable");
        }

        // Get appointments for this patient
        List<AppointmentResponseDTO> appointments = getAppointmentsByPatient(patientId);

        // Combine patient data with appointments
        Map<String, Object> result = new HashMap<>();
        result.put("patient", patientData);
        result.put("appointments", appointments);
        result.put("totalAppointments", appointments.size());

        return result;
    }

    private String getPatientName(UUID patientId) {
        try {
            String patientServiceUrl = "http://java-spring-microservices-patient-service-1:4000/patients/" + patientId;
            Map<String, Object> patientData = restTemplate.getForObject(patientServiceUrl, Map.class);
            return patientData != null ? (String) patientData.get("name") : "Unknown Patient";
        } catch (Exception e) {
            return "Unknown Patient";
        }
    }

    private AppointmentResponseDTO mapToResponseDTO(Appointment appointment, String patientName, String doctorName) {
        return new AppointmentResponseDTO(
            appointment.getId(),
            appointment.getPatientId(),
            patientName,
            appointment.getDoctorId(),
            doctorName,
            appointment.getAppointmentDate(),
            appointment.getAppointmentTime(),
            appointment.getReason(),
            appointment.getNotes(),
            appointment.getStatus()
        );
    }
}