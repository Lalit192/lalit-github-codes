package com.pm.notificationservice.controller;

import com.pm.notificationservice.model.Notification;
import com.pm.notificationservice.service.NotificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/notifications")
@CrossOrigin(origins = "*")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping
    public ResponseEntity<List<Notification>> getAllNotifications() {
        List<Notification> notifications = notificationService.getAllNotifications();
        return ResponseEntity.ok(notifications);
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<List<Notification>> getNotificationsByEmail(@PathVariable String email) {
        List<Notification> notifications = notificationService.getNotificationsByEmail(email);
        return ResponseEntity.ok(notifications);
    }

    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getNotificationStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalNotifications", notificationService.getAllNotifications().size());
        stats.put("pendingNotifications", notificationService.getPendingNotificationsCount());
        stats.put("sentNotifications", notificationService.getSentNotificationsCount());
        return ResponseEntity.ok(stats);
    }

    @PostMapping("/test-welcome")
    public ResponseEntity<Notification> sendTestWelcomeEmail(
            @RequestParam String email,
            @RequestParam String name,
            @RequestParam String patientId) {
        
        Notification notification = notificationService.sendWelcomeEmail(email, name, patientId);
        return ResponseEntity.ok(notification);
    }
}