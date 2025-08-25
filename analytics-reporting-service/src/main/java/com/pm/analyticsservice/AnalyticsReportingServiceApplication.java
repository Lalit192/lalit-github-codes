package com.pm.analyticsservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.kafka.annotation.EnableKafka;

@SpringBootApplication
@EnableKafka
@EnableCaching
public class AnalyticsReportingServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(AnalyticsReportingServiceApplication.class, args);
    }
}