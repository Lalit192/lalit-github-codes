package com.pm.doctorservice.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "doctors")
public class Doctor {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String specialization;

    @Column(nullable = false)
    private String phoneNumber;

    @Column(nullable = false)
    private String licenseNumber;

    @Column(nullable = false)
    private Integer experienceYears;

    @Column(nullable = false)
    private LocalDate joinedDate;

    @Column(nullable = false)
    private String department;

    @Column
    private String address;

    // Constructors
    public Doctor() {}

    public Doctor(String name, String email, String specialization, String phoneNumber, 
                  String licenseNumber, Integer experienceYears, LocalDate joinedDate, 
                  String department, String address) {
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