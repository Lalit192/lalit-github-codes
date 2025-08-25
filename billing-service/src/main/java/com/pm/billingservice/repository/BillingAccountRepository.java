package com.pm.billingservice.repository;

import com.pm.billingservice.model.BillingAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface BillingAccountRepository extends JpaRepository<BillingAccount, UUID> {
    
    Optional<BillingAccount> findByPatientId(String patientId);
    Optional<BillingAccount> findByAccountNumber(String accountNumber);
    boolean existsByPatientId(String patientId);
}