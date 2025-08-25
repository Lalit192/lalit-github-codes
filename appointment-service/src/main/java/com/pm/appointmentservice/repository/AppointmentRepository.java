package com.pm.appointmentservice.repository;

import com.pm.appointmentservice.model.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, UUID> {
    
    List<Appointment> findByPatientIdOrderByAppointmentDateTimeDesc(String patientId);
    List<Appointment> findByDoctorIdOrderByAppointmentDateTimeDesc(String doctorId);
    List<Appointment> findByStatusOrderByAppointmentDateTimeDesc(Appointment.AppointmentStatus status);
    
    @Query("SELECT a FROM Appointment a WHERE a.doctorId = ?1 AND a.appointmentDateTime BETWEEN ?2 AND ?3 AND a.status != 'CANCELLED'")
    List<Appointment> findDoctorAppointmentsInTimeRange(String doctorId, LocalDateTime start, LocalDateTime end);
    
    long countByStatus(Appointment.AppointmentStatus status);
    
    @Query("SELECT COUNT(a) FROM Appointment a WHERE a.appointmentDateTime >= ?1 AND a.appointmentDateTime < ?2")
    long countAppointmentsForDate(LocalDateTime startOfDay, LocalDateTime endOfDay);
}