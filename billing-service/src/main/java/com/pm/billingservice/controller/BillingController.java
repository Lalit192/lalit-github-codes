package com.pm.billingservice.controller;

import com.pm.billingservice.model.BillingAccount;
import com.pm.billingservice.repository.BillingAccountRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/billing-accounts")
public class BillingController {

    private final BillingAccountRepository billingAccountRepository;
    
    public BillingController(BillingAccountRepository billingAccountRepository) {
        this.billingAccountRepository = billingAccountRepository;
    }

    @GetMapping
    public List<BillingAccount> getAllBillingAccounts() {
        return billingAccountRepository.findAll();
    }
}