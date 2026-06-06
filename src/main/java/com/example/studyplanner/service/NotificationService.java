package com.example.studyplanner.service;

import com.example.studyplanner.model.Notification;
import com.example.studyplanner.model.Task;
import com.example.studyplanner.repository.NotificationRepository;
import com.example.studyplanner.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private JavaMailSender mailSender;

    public List<Notification> getNotificationsByUser(Long userId) {
        return notificationRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }

    public void markAsRead(Long id) {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notification not found"));
        notification.setRead(true);
        notificationRepository.save(notification);
    }

    @Scheduled(cron = "0 * * * * *")
    public void checkUpcomingDeadlines() {
        System.out.println(">>> Scheduler running at: " + LocalDateTime.now());

        List<Task> allTasks = taskRepository.findAll();
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime tomorrow = now.plusDays(1);

        for (Task task : allTasks) {
            if (!task.isCompleted() && task.getDeadline() != null) {
                if (task.getDeadline().isBefore(tomorrow) && task.getDeadline().isAfter(now)) {

                    String alertMessage = "Deadline Reminder: \"" + task.getTitle() + "\" is due on "
                            + task.getDeadline().toLocalDate() + " at "
                            + task.getDeadline().toLocalTime().withSecond(0).withNano(0);

                    boolean alreadyNotified = notificationRepository
                            .findByUserIdOrderByCreatedAtDesc(task.getUser().getId())
                            .stream().anyMatch(n -> n.getMessage().equals(alertMessage));

                    if (!alreadyNotified) {
                        // Save in-app notification
                        Notification notification = new Notification();
                        notification.setMessage(alertMessage);
                        notification.setUser(task.getUser());
                        notificationRepository.save(notification);

                        // Send email
                        sendDeadlineEmail(task, alertMessage);

                        System.out.println(">>> Notification saved and email sent for: " + task.getTitle());
                    } else {
                        System.out.println(">>> Already notified, skipping: " + task.getTitle());
                    }
                }
            }
        }
    }

    private void sendDeadlineEmail(Task task, String alertMessage) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(task.getUser().getEmail());
            message.setSubject(" Study Planner — Deadline Reminder");
            message.setText(
                "Hi " + task.getUser().getUsername() + ",\n\n" +
                "This is a reminder that you have an upcoming task deadline:\n\n" +
                " Task: " + task.getTitle() + "\n" +
                (task.getDescription() != null && !task.getDescription().isEmpty()
                    ? " Description: " + task.getDescription() + "\n" : "") +
                " Due: " + task.getDeadline().toLocalDate() + " at " +
                task.getDeadline().toLocalTime().withSecond(0).withNano(0) + "\n\n" +
                "Log in to your Study Planner to mark it complete.\n\n" +
                "Good luck! \n" +
                "— My Study Planner"
            );
            mailSender.send(message);
            System.out.println(">>> Email sent to: " + task.getUser().getEmail());
        } catch (Exception e) {
            System.out.println(">>> Failed to send email: " + e.getMessage());
        }
    }
}