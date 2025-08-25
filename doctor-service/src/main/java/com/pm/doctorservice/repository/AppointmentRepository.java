package com.pm.doctorservice.repository;

import com.pm.doctorservice.model.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, UUID> {
    
    List<Appointment> findByDoctorId(UUID doctorId);
    
    List<Appointment> findByPatientId(UUID patientId);
    
    List<Appointment> findByDoctorIdAndStatus(UUID doctorId, String status);
    
    List<Appointment> findByPatientIdAndStatus(UUID patientId, String status);
}