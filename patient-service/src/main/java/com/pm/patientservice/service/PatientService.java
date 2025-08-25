package com.pm.patientservice.service;

import com.pm.patientservice.dto.PatientRequestDTO;
import com.pm.patientservice.dto.PatientResponseDTO;
import com.pm.patientservice.exception.EmailAlreadyExistsException;
import com.pm.patientservice.exception.PatientNotFoundException;
import com.pm.patientservice.grpc.BillingServiceGrpcClient;
import com.pm.patientservice.kafka.KafkaProducer;
import com.pm.patientservice.kafka.PatientEventPublisher;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CacheEvict;
import com.pm.patientservice.mapper.PatientMapper;
import com.pm.patientservice.model.Patient;
import com.pm.patientservice.repository.PatientRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class PatientService {

  private final PatientRepository patientRepository;
  private final BillingServiceGrpcClient billingServiceGrpcClient;
  private final KafkaProducer kafkaProducer;
  private final PatientEventPublisher patientEventPublisher;

  public PatientService(PatientRepository patientRepository,
      BillingServiceGrpcClient billingServiceGrpcClient,
      KafkaProducer kafkaProducer,
      PatientEventPublisher patientEventPublisher) {
    this.patientRepository = patientRepository;
    this.billingServiceGrpcClient = billingServiceGrpcClient;
    this.kafkaProducer = kafkaProducer;
    this.patientEventPublisher = patientEventPublisher;
  }

  @Cacheable("patients")
  public List<PatientResponseDTO> getPatients() {
    List<Patient> patients = patientRepository.findAll();

    return patients.stream().map(PatientMapper::toDTO).toList();
  }

  public PatientResponseDTO getPatientById(UUID id) {
    Patient patient = patientRepository.findById(id).orElseThrow(
        () -> new PatientNotFoundException("Patient not found with ID: " + id));
    return PatientMapper.toDTO(patient);
  }

  @CacheEvict(value = "patients", allEntries = true)
  public PatientResponseDTO createPatient(PatientRequestDTO patientRequestDTO) {
    if (patientRepository.existsByEmail(patientRequestDTO.getEmail())) {
      throw new EmailAlreadyExistsException(
          "A patient with this email " + "already exists"
              + patientRequestDTO.getEmail());
    }

    Patient newPatient = patientRepository.save(
        PatientMapper.toModel(patientRequestDTO));

    billingServiceGrpcClient.createBillingAccount(
        newPatient.getId().toString(),
        newPatient.getName(), 
        newPatient.getEmail(),
        newPatient.getDateOfBirth().toString(),
        newPatient.getAddress());

    kafkaProducer.sendEvent(newPatient);
    
    // Publish event for notification service
    patientEventPublisher.publishPatientCreatedEvent(newPatient);

    return PatientMapper.toDTO(newPatient);
  }

  public PatientResponseDTO updatePatient(UUID id,
      PatientRequestDTO patientRequestDTO) {

    Patient patient = patientRepository.findById(id).orElseThrow(
        () -> new PatientNotFoundException("Patient not found with ID: " + id));

    if (patientRepository.existsByEmailAndIdNot(patientRequestDTO.getEmail(),
        id)) {
      throw new EmailAlreadyExistsException(
          "A patient with this email " + "already exists"
              + patientRequestDTO.getEmail());
    }

    patient.setName(patientRequestDTO.getName());
    patient.setAddress(patientRequestDTO.getAddress());
    patient.setEmail(patientRequestDTO.getEmail());
    patient.setDateOfBirth(LocalDate.parse(patientRequestDTO.getDateOfBirth()));

    Patient updatedPatient = patientRepository.save(patient);
    return PatientMapper.toDTO(updatedPatient);
  }

  public void deletePatient(UUID id) {
    patientRepository.deleteById(id);
  }
}
