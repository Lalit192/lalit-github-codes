package com.pm.appointmentservice.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;

@Service
public class PatientServiceClient {

    private static final Logger log = LoggerFactory.getLogger(PatientServiceClient.class);
    
    private final WebClient webClient;

    public PatientServiceClient(@Value("${patient.service.url}") String patientServiceUrl) {
        this.webClient = WebClient.builder()
                .baseUrl(patientServiceUrl)
                .build();
    }

@SuppressWarnings("unchecked")
    public Mono<Map<String, Object>> getPatientById(String patientId) {
        return webClient.get()
                .uri("/patients/{id}", patientId)
                .retrieve()
                .bodyToMono(Map.class)
                .map(map -> (Map<String, Object>) map)
                .doOnSuccess(patient -> log.info("✅ Retrieved patient: {}", patient.get("name")))
                .doOnError(error -> log.error("❌ Failed to retrieve patient {}: {}", patientId, error.getMessage()));
    }

    public Mono<Boolean> validatePatientExists(String patientId) {
        return getPatientById(patientId)
                .map(patient -> true)
                .onErrorReturn(false);
    }
}