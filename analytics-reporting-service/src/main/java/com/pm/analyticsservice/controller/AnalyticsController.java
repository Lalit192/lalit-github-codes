package com.pm.analyticsservice.controller;

import com.pm.analyticsservice.service.AnalyticsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.Map;

@RestController
@RequestMapping("/analytics")
@CrossOrigin(origins = "*")
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    public AnalyticsController(AnalyticsService analyticsService) {
        this.analyticsService = analyticsService;
    }

    @GetMapping("/dashboard")
    public Mono<ResponseEntity<Map<String, Object>>> getDashboardStats() {
        return analyticsService.getDashboardStats()
                .map(ResponseEntity::ok)
                .onErrorReturn(ResponseEntity.internalServerError().build());
    }

    @GetMapping("/revenue")
    public Mono<ResponseEntity<Map<String, Object>>> getRevenueReport() {
        return analyticsService.getRevenueReport()
                .map(ResponseEntity::ok)
                .onErrorReturn(ResponseEntity.internalServerError().build());
    }

    @GetMapping("/appointments")
    public Mono<ResponseEntity<Map<String, Object>>> getAppointmentAnalytics() {
        return analyticsService.getAppointmentAnalytics()
                .map(ResponseEntity::ok)
                .onErrorReturn(ResponseEntity.internalServerError().build());
    }

    @GetMapping("/health")
    public Mono<ResponseEntity<Map<String, Object>>> getSystemHealthReport() {
        return analyticsService.getSystemHealthReport()
                .map(ResponseEntity::ok)
                .onErrorReturn(ResponseEntity.internalServerError().build());
    }
}