package com.pm.doctorservice.repository;

import com.pm.doctorservice.model.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, UUID> {
    
    Optional<Doctor> findByEmail(String email);
    
    List<Doctor> findBySpecialization(String specialization);
    
    List<Doctor> findByDepartment(String department);
    
    @Query("SELECT d FROM Doctor d WHERE d.experienceYears >= :minYears")
    List<Doctor> findByMinimumExperience(@Param("minYears") Integer minYears);
    
    boolean existsByEmail(String email);
    
    boolean existsByLicenseNumber(String licenseNumber);
}