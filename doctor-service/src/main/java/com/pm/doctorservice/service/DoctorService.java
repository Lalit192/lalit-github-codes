package com.pm.doctorservice.service;

import com.pm.doctorservice.dto.DoctorRequestDTO;
import com.pm.doctorservice.dto.DoctorResponseDTO;
import com.pm.doctorservice.model.Doctor;
import com.pm.doctorservice.repository.DoctorRepository;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class DoctorService {

    private final DoctorRepository doctorRepository;
    private final KafkaTemplate<String, String> kafkaTemplate;

    public DoctorService(DoctorRepository doctorRepository, KafkaTemplate<String, String> kafkaTemplate) {
        this.doctorRepository = doctorRepository;
        this.kafkaTemplate = kafkaTemplate;
    }

    public DoctorResponseDTO createDoctor(DoctorRequestDTO requestDTO) {
        if (doctorRepository.existsByEmail(requestDTO.getEmail())) {
            throw new RuntimeException("Email already exists");
        }
        
        if (doctorRepository.existsByLicenseNumber(requestDTO.getLicenseNumber())) {
            throw new RuntimeException("License number already exists");
        }

        Doctor doctor = new Doctor(
            requestDTO.getName(),
            requestDTO.getEmail(),
            requestDTO.getSpecialization(),
            requestDTO.getPhoneNumber(),
            requestDTO.getLicenseNumber(),
            requestDTO.getExperienceYears(),
            LocalDate.parse(requestDTO.getJoinedDate()),
            requestDTO.getDepartment(),
            requestDTO.getAddress()
        );

        Doctor savedDoctor = doctorRepository.save(doctor);
        
        // Send Kafka event
        kafkaTemplate.send("doctor-events", "Doctor created: " + savedDoctor.getId());
        
        return mapToResponseDTO(savedDoctor);
    }

    public List<DoctorResponseDTO> getAllDoctors() {
        return doctorRepository.findAll().stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    public Optional<DoctorResponseDTO> getDoctorById(UUID id) {
        return doctorRepository.findById(id)
                .map(this::mapToResponseDTO);
    }

    public List<DoctorResponseDTO> getDoctorsBySpecialization(String specialization) {
        return doctorRepository.findBySpecialization(specialization).stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    public List<DoctorResponseDTO> getDoctorsByDepartment(String department) {
        return doctorRepository.findByDepartment(department).stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    public DoctorResponseDTO updateDoctor(UUID id, DoctorRequestDTO requestDTO) {
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Doctor not found"));

        doctor.setName(requestDTO.getName());
        doctor.setSpecialization(requestDTO.getSpecialization());
        doctor.setPhoneNumber(requestDTO.getPhoneNumber());
        doctor.setExperienceYears(requestDTO.getExperienceYears());
        doctor.setDepartment(requestDTO.getDepartment());
        doctor.setAddress(requestDTO.getAddress());

        Doctor updatedDoctor = doctorRepository.save(doctor);
        
        // Send Kafka event
        kafkaTemplate.send("doctor-events", "Doctor updated: " + updatedDoctor.getId());
        
        return mapToResponseDTO(updatedDoctor);
    }

    public void deleteDoctor(UUID id) {
        if (!doctorRepository.existsById(id)) {
            throw new RuntimeException("Doctor not found");
        }
        doctorRepository.deleteById(id);
        
        // Send Kafka event
        kafkaTemplate.send("doctor-events", "Doctor deleted: " + id);
    }

    private DoctorResponseDTO mapToResponseDTO(Doctor doctor) {
        return new DoctorResponseDTO(
            doctor.getId(),
            doctor.getName(),
            doctor.getEmail(),
            doctor.getSpecialization(),
            doctor.getPhoneNumber(),
            doctor.getLicenseNumber(),
            doctor.getExperienceYears(),
            doctor.getJoinedDate(),
            doctor.getDepartment(),
            doctor.getAddress()
        );
    }
}