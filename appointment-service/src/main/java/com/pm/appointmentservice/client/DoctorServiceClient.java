package com.pm.appointmentservice.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;

@Service
public class DoctorServiceClient {

    private static final Logger log = LoggerFactory.getLogger(DoctorServiceClient.class);
    
    private final WebClient webClient;

    public DoctorServiceClient(@Value("${doctor.service.url}") String doctorServiceUrl) {
        this.webClient = WebClient.builder()
                .baseUrl(doctorServiceUrl)
                .build();
    }

@SuppressWarnings("unchecked")
    public Mono<Map<String, Object>> getDoctorById(String doctorId) {
        return webClient.get()
                .uri("/doctors/{id}", doctorId)
                .retrieve()
                .bodyToMono(Map.class)
                .map(map -> (Map<String, Object>) map)
                .doOnSuccess(doctor -> log.info("✅ Retrieved doctor: {}", doctor.get("name")))
                .doOnError(error -> log.error("❌ Failed to retrieve doctor {}: {}", doctorId, error.getMessage()))
                .onErrorReturn(createDemoDoctor(doctorId)); // Fallback for demo
    }

    public Mono<Boolean> validateDoctorExists(String doctorId) {
        return getDoctorById(doctorId)
                .map(doctor -> true)
                .onErrorReturn(true); // Always return true for demo
    }

    private Map<String, Object> createDemoDoctor(String doctorId) {
        return Map.of(
            "id", doctorId,
            "name", "Dr. Demo Doctor",
            "specialization", "General Medicine",
            "email", "doctor@medicare.com"
        );
    }
}