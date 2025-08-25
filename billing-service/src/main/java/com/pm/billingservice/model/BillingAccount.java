package com.pm.billingservice.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "billing_accounts")
public class BillingAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(unique = true, nullable = false)
    private String accountNumber;

    @Column(nullable = false)
    private String patientId;

    @Column(nullable = false)
    private String patientName;

    @Column(nullable = false)
    private String email;

    private LocalDate dateOfBirth;

    private String address;

    @Enumerated(EnumType.STRING)
    private AccountStatus status = AccountStatus.ACTIVE;

    private LocalDateTime createdAt = LocalDateTime.now();

    private String createdVia = "gRPC";

    // Constructors
    public BillingAccount() {}

    public BillingAccount(String accountNumber, String patientId, String patientName, 
                         String email, LocalDate dateOfBirth, String address) {
        this.accountNumber = accountNumber;
        this.patientId = patientId;
        this.patientName = patientName;
        this.email = email;
        this.dateOfBirth = dateOfBirth;
        this.address = address;
    }

    // Getters and Setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public String getAccountNumber() { return accountNumber; }
    public void setAccountNumber(String accountNumber) { this.accountNumber = accountNumber; }

    public String getPatientId() { return patientId; }
    public void setPatientId(String patientId) { this.patientId = patientId; }

    public String getPatientName() { return patientName; }
    public void setPatientName(String patientName) { this.patientName = patientName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public LocalDate getDateOfBirth() { return dateOfBirth; }
    public void setDateOfBirth(LocalDate dateOfBirth) { this.dateOfBirth = dateOfBirth; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public AccountStatus getStatus() { return status; }
    public void setStatus(AccountStatus status) { this.status = status; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public String getCreatedVia() { return createdVia; }
    public void setCreatedVia(String createdVia) { this.createdVia = createdVia; }

    public enum AccountStatus {
        ACTIVE, INACTIVE, SUSPENDED
    }
}