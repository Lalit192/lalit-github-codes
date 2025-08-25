package com.pm.notificationservice.service;

import com.pm.notificationservice.model.Notification;
import com.pm.notificationservice.repository.NotificationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class NotificationService {

    private static final Logger log = LoggerFactory.getLogger(NotificationService.class);
    
    private final NotificationRepository notificationRepository;
    private final JavaMailSender mailSender;

    public NotificationService(NotificationRepository notificationRepository, JavaMailSender mailSender) {
        this.notificationRepository = notificationRepository;
        this.mailSender = mailSender;
    }

    public Notification sendWelcomeEmail(String patientEmail, String patientName, String patientId) {
        String subject = "Welcome to MediCare Hospital!";
        String message = String.format(
            "Dear %s,\n\n" +
            "Welcome to MediCare Hospital Management System!\n\n" +
            "Your patient account has been successfully created.\n" +
            "Patient ID: %s\n\n" +
            "We're committed to providing you with the best healthcare services.\n\n" +
            "Best regards,\n" +
            "MediCare Hospital Team",
            patientName, patientId
        );

        Notification notification = new Notification(
            patientEmail, patientName, subject, message,
            Notification.NotificationType.PATIENT_WELCOME,
            "patient-service", patientId
        );

        return sendNotification(notification);
    }

    public Notification sendBillingReminder(String patientEmail, String patientName, String accountNumber) {
        String subject = "Billing Account Created - MediCare Hospital";
        String message = String.format(
            "Dear %s,\n\n" +
            "Your billing account has been successfully created.\n" +
            "Account Number: %s\n\n" +
            "You can now view your billing information and make payments through our system.\n\n" +
            "Best regards,\n" +
            "MediCare Billing Department",
            patientName, accountNumber
        );

        Notification notification = new Notification(
            patientEmail, patientName, subject, message,
            Notification.NotificationType.BILLING_REMINDER,
            "billing-service", accountNumber
        );

        return sendNotification(notification);
    }

    public Notification sendAppointmentConfirmation(String patientEmail, String patientName, 
                                                   String doctorName, String appointmentDateTime, String appointmentId) {
        String subject = "Appointment Confirmation - MediCare Hospital";
        String message = String.format(
            "Dear %s,\n\n" +
            "Your appointment has been successfully scheduled!\n\n" +
            "Appointment Details:\n" +
            "Doctor: %s\n" +
            "Date & Time: %s\n" +
            "Appointment ID: %s\n\n" +
            "Please arrive 15 minutes before your scheduled time.\n\n" +
            "Best regards,\n" +
            "MediCare Hospital Team",
            patientName, doctorName, appointmentDateTime, appointmentId
        );

        Notification notification = new Notification(
            patientEmail, patientName, subject, message,
            Notification.NotificationType.APPOINTMENT_CONFIRMATION,
            "appointment-service", appointmentId
        );

        return sendNotification(notification);
    }

    private Notification sendNotification(Notification notification) {
        try {
            // Save notification to database first
            Notification savedNotification = notificationRepository.save(notification);
            
            // Send email
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setTo(notification.getRecipientEmail());
            mailMessage.setSubject(notification.getSubject());
            mailMessage.setText(notification.getMessage());
            mailMessage.setFrom("noreply@medicare.com");

            // For demo purposes, we'll just log the email instead of actually sending
            log.info("📧 EMAIL SENT TO: {}", notification.getRecipientEmail());
            log.info("📧 SUBJECT: {}", notification.getSubject());
            log.info("📧 MESSAGE: {}", notification.getMessage());
            
            // mailSender.send(mailMessage); // Uncomment to actually send emails
            
            // Update notification status
            savedNotification.setStatus(Notification.NotificationStatus.SENT);
            savedNotification.setSentAt(LocalDateTime.now());
            
            return notificationRepository.save(savedNotification);
            
        } catch (Exception e) {
            log.error("Failed to send notification: {}", e.getMessage());
            notification.setStatus(Notification.NotificationStatus.FAILED);
            return notificationRepository.save(notification);
        }
    }

    public List<Notification> getAllNotifications() {
        return notificationRepository.findAll();
    }

    public List<Notification> getNotificationsByEmail(String email) {
        return notificationRepository.findByRecipientEmailOrderByCreatedAtDesc(email);
    }

    public long getPendingNotificationsCount() {
        return notificationRepository.countByStatus(Notification.NotificationStatus.PENDING);
    }

    public long getSentNotificationsCount() {
        return notificationRepository.countByStatus(Notification.NotificationStatus.SENT);
    }
}