package com.pm.notificationservice.repository;

import com.pm.notificationservice.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, UUID> {
    
    List<Notification> findByRecipientEmailOrderByCreatedAtDesc(String recipientEmail);
    List<Notification> findByStatusOrderByCreatedAtDesc(Notification.NotificationStatus status);
    List<Notification> findBySourceServiceOrderByCreatedAtDesc(String sourceService);
    long countByStatus(Notification.NotificationStatus status);
}