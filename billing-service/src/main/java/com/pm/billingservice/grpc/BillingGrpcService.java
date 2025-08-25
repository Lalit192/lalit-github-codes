package com.pm.billingservice.grpc;

import billing.BillingRequest;
import billing.BillingResponse;
import billing.BillingServiceGrpc.BillingServiceImplBase;
import com.pm.billingservice.model.BillingAccount;
import com.pm.billingservice.repository.BillingAccountRepository;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;

@GrpcService
public class BillingGrpcService extends BillingServiceImplBase {

  private static final Logger log = LoggerFactory.getLogger(
      BillingGrpcService.class);
  
  private final BillingAccountRepository billingAccountRepository;
  
  public BillingGrpcService(BillingAccountRepository billingAccountRepository) {
    this.billingAccountRepository = billingAccountRepository;
  }

  @Override
  public void createBillingAccount(BillingRequest billingRequest,
      StreamObserver<BillingResponse> responseObserver) {

      log.info("createBillingAccount request received {}", billingRequest.toString());

      try {
        // Check if account already exists
        if (billingAccountRepository.existsByPatientId(billingRequest.getPatientId())) {
          log.warn("Billing account already exists for patient: {}", billingRequest.getPatientId());
          
          BillingResponse response = BillingResponse.newBuilder()
              .setAccountId("EXISTING")
              .setStatus("ALREADY_EXISTS")
              .build();
          
          responseObserver.onNext(response);
          responseObserver.onCompleted();
          return;
        }

        // Generate unique account number
        String accountNumber = "BA-" + System.currentTimeMillis();
        
        // Parse date of birth safely
        LocalDate dateOfBirth = null;
        if (billingRequest.getDateOfBirth() != null && !billingRequest.getDateOfBirth().isEmpty()) {
          try {
            dateOfBirth = LocalDate.parse(billingRequest.getDateOfBirth());
          } catch (Exception e) {
            log.warn("Invalid date format: {}", billingRequest.getDateOfBirth());
            dateOfBirth = LocalDate.now().minusYears(30);
          }
        } else {
          dateOfBirth = LocalDate.now().minusYears(30);
        }
        
        // Create billing account entity
        BillingAccount billingAccount = new BillingAccount(
            accountNumber,
            billingRequest.getPatientId(),
            billingRequest.getName(),
            billingRequest.getEmail(),
            dateOfBirth,
            billingRequest.getAddress()
        );
        
        // Save to database
        BillingAccount savedAccount = billingAccountRepository.save(billingAccount);
        
        log.info("Billing account created: {} for patient: {}", 
                savedAccount.getAccountNumber(), savedAccount.getPatientName());

        BillingResponse response = BillingResponse.newBuilder()
            .setAccountId(savedAccount.getAccountNumber())
            .setStatus("ACTIVE")
            .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
        
      } catch (Exception e) {
        log.error("Error creating billing account: {}", e.getMessage());
        
        BillingResponse response = BillingResponse.newBuilder()
            .setAccountId("ERROR")
            .setStatus("FAILED")
            .build();
        
        responseObserver.onNext(response);
        responseObserver.onCompleted();
      }
  }
}
