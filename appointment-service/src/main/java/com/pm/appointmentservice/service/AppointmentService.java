package com.pm.appointmentservice.service;

import com.pm.appointmentservice.client.DoctorServiceClient;
import com.pm.appointmentservice.client.PatientServiceClient;
import com.pm.appointmentservice.dto.AppointmentRequestDTO;
import com.pm.appointmentservice.kafka.AppointmentEventPublisher;
import com.pm.appointmentservice.model.Appointment;
import com.pm.appointmentservice.repository.AppointmentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class AppointmentService {

    private static final Logger log = LoggerFactory.getLogger(AppointmentService.class);
    
    private final AppointmentRepository appointmentRepository;
    private final PatientServiceClient patientServiceClient;
    private final DoctorServiceClient doctorServiceClient;
    private final AppointmentEventPublisher eventPublisher;

    public AppointmentService(AppointmentRepository appointmentRepository,
                             PatientServiceClient patientServiceClient,
                             DoctorServiceClient doctorServiceClient,
                             AppointmentEventPublisher eventPublisher) {
        this.appointmentRepository = appointmentRepository;
        this.patientServiceClient = patientServiceClient;
        this.doctorServiceClient = doctorServiceClient;
        this.eventPublisher = eventPublisher;
    }

    public Mono<Appointment> createAppointment(AppointmentRequestDTO request) {
        log.info("🗓️ Creating appointment for patient: {} with doctor: {}", request.getPatientId(), request.getDoctorId());
        
        // Validate patient and doctor exist (parallel calls)
        Mono<Map<String, Object>> patientMono = patientServiceClient.getPatientById(request.getPatientId());
        Mono<Map<String, Object>> doctorMono = doctorServiceClient.getDoctorById(request.getDoctorId());
        
        return Mono.zip(patientMono, doctorMono)
                .flatMap(tuple -> {
                    Map<String, Object> patient = tuple.getT1();
                    Map<String, Object> doctor = tuple.getT2();
                    
                    // Check doctor availability
                    return checkDoctorAvailability(request.getDoctorId(), request.getAppointmentDateTime())
                            .flatMap(isAvailable -> {
                                if (!isAvailable) {
                                    return Mono.error(new RuntimeException("Doctor is not available at the requested time"));
                                }
                                
                                // Create appointment
                                Appointment appointment = new Appointment(
                                    request.getPatientId(),
                                    (String) patient.get("name"),
                                    (String) patient.get("email"),
                                    request.getDoctorId(),
                                    (String) doctor.get("name"),
                                    request.getAppointmentDateTime(),
                                    request.getType(),
                                    request.getNotes()
                                );
                                
                                // Save appointment
                                Appointment savedAppointment = appointmentRepository.save(appointment);
                                
                                // Publish event for notification service
                                eventPublisher.publishAppointmentCreatedEvent(savedAppointment);
                                
                                log.info("✅ Appointment created successfully: {}", savedAppointment.getId());
                                return Mono.just(savedAppointment);
                            });
                })
                .onErrorMap(throwable -> {
                    log.error("❌ Failed to create appointment: {}", throwable.getMessage());
                    return new RuntimeException("Failed to create appointment: " + throwable.getMessage());
                });
    }

    private Mono<Boolean> checkDoctorAvailability(String doctorId, LocalDateTime appointmentTime) {
        // Check if doctor has conflicting appointments (±30 minutes)
        LocalDateTime start = appointmentTime.minusMinutes(30);
        LocalDateTime end = appointmentTime.plusMinutes(30);
        
        List<Appointment> conflictingAppointments = appointmentRepository
                .findDoctorAppointmentsInTimeRange(doctorId, start, end);
        
        boolean isAvailable = conflictingAppointments.isEmpty();
        log.info("🔍 Doctor {} availability check: {}", doctorId, isAvailable ? "Available" : "Busy");
        
        return Mono.just(isAvailable);
    }

    public List<Appointment> getAllAppointments() {
        return appointmentRepository.findAll();
    }

    public List<Appointment> getAppointmentsByPatient(String patientId) {
        return appointmentRepository.findByPatientIdOrderByAppointmentDateTimeDesc(patientId);
    }

    public List<Appointment> getAppointmentsByDoctor(String doctorId) {
        return appointmentRepository.findByDoctorIdOrderByAppointmentDateTimeDesc(doctorId);
    }

    public Appointment updateAppointmentStatus(UUID appointmentId, Appointment.AppointmentStatus status) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));
        
        appointment.setStatus(status);
        appointment.setUpdatedAt(LocalDateTime.now());
        
        Appointment updatedAppointment = appointmentRepository.save(appointment);
        
        // Publish status update event
        eventPublisher.publishAppointmentStatusUpdatedEvent(updatedAppointment);
        
        return updatedAppointment;
    }

    public long getAppointmentStats(String type) {
        return switch (type.toLowerCase()) {
            case "scheduled" -> appointmentRepository.countByStatus(Appointment.AppointmentStatus.SCHEDULED);
            case "completed" -> appointmentRepository.countByStatus(Appointment.AppointmentStatus.COMPLETED);
            case "cancelled" -> appointmentRepository.countByStatus(Appointment.AppointmentStatus.CANCELLED);
            default -> appointmentRepository.count();
        };
    }
}