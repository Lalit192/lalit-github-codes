package com.pm.doctorservice.dto;

import java.time.LocalDate;
import java.util.UUID;

public class DoctorResponseDTO {

    private UUID id;
    private String name;
    private String email;
    private String specialization;
    private String phoneNumber;
    private String licenseNumber;
    private Integer experienceYears;
    private LocalDate joinedDate;
    private String department;
    private String address;

    // Constructors
    public DoctorResponseDTO() {}

    public DoctorResponseDTO(UUID id, String name, String email, String specialization, 
                           String phoneNumber, String licenseNumber, Integer experienceYears, 
                           LocalDate joinedDate, String department, String address) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.specialization = specialization;
        this.phoneNumber = phoneNumber;
        this.licenseNumber = licenseNumber;
        this.experienceYears = experienceYears;
        this.joinedDate = joinedDate;
        this.department = department;
        this.address = address;
    }

    // Getters and Setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getSpecialization() { return specialization; }
    public void setSpecialization(String specialization) { this.specialization = specialization; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public String getLicenseNumber() { return licenseNumber; }
    public void setLicenseNumber(String licenseNumber) { this.licenseNumber = licenseNumber; }

    public Integer getExperienceYears() { return experienceYears; }
    public void setExperienceYears(Integer experienceYears) { this.experienceYears = experienceYears; }

    public LocalDate getJoinedDate() { return joinedDate; }
    public void setJoinedDate(LocalDate joinedDate) { this.joinedDate = joinedDate; }

    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
}