package com.pm.doctorservice.controller;

import com.pm.doctorservice.dto.DoctorRequestDTO;
import com.pm.doctorservice.dto.DoctorResponseDTO;
import com.pm.doctorservice.service.DoctorService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/doctors")
public class DoctorController {

    private final DoctorService doctorService;

    public DoctorController(DoctorService doctorService) {
        this.doctorService = doctorService;
    }

    @Operation(summary = "Create a new doctor")
    @PostMapping
    public ResponseEntity<DoctorResponseDTO> createDoctor(@Valid @RequestBody DoctorRequestDTO requestDTO) {
        try {
            DoctorResponseDTO doctor = doctorService.createDoctor(requestDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(doctor);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(summary = "Get all doctors")
    @GetMapping
    public ResponseEntity<List<DoctorResponseDTO>> getAllDoctors() {
        List<DoctorResponseDTO> doctors = doctorService.getAllDoctors();
        return ResponseEntity.ok(doctors);
    }

    @Operation(summary = "Get doctor by ID")
    @GetMapping("/{id}")
    public ResponseEntity<DoctorResponseDTO> getDoctorById(@PathVariable UUID id) {
        Optional<DoctorResponseDTO> doctor = doctorService.getDoctorById(id);
        return doctor.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Get doctors by specialization")
    @GetMapping("/specialization/{specialization}")
    public ResponseEntity<List<DoctorResponseDTO>> getDoctorsBySpecialization(@PathVariable String specialization) {
        List<DoctorResponseDTO> doctors = doctorService.getDoctorsBySpecialization(specialization);
        return ResponseEntity.ok(doctors);
    }

    @Operation(summary = "Get doctors by department")
    @GetMapping("/department/{department}")
    public ResponseEntity<List<DoctorResponseDTO>> getDoctorsByDepartment(@PathVariable String department) {
        List<DoctorResponseDTO> doctors = doctorService.getDoctorsByDepartment(department);
        return ResponseEntity.ok(doctors);
    }

    @Operation(summary = "Update doctor")
    @PutMapping("/{id}")
    public ResponseEntity<DoctorResponseDTO> updateDoctor(@PathVariable UUID id, @Valid @RequestBody DoctorRequestDTO requestDTO) {
        try {
            DoctorResponseDTO doctor = doctorService.updateDoctor(id, requestDTO);
            return ResponseEntity.ok(doctor);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Delete doctor")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDoctor(@PathVariable UUID id) {
        try {
            doctorService.deleteDoctor(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}