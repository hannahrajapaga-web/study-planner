package com.example.studyplanner.service;

import com.example.studyplanner.model.Notification;
import com.example.studyplanner.model.Task;
import com.example.studyplanner.repository.NotificationRepository;
import com.example.studyplanner.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private NotificationRepository notificationRepository;

    public List<Task> getTasksByUser(Long userId) {
        return taskRepository.findByUserId(userId);
    }

    public Task createTask(Task task) {
        if (task.isCompleted() == null) {
            task.setCompleted(false);
        }
        if (task.getDeadline() != null && task.getDeadline().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Deadline cannot be in the past");
        }
        return taskRepository.save(task);
    }

    public Task updateTask(Long id, Task updatedTask) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found"));
        task.setTitle(updatedTask.getTitle());
        task.setDescription(updatedTask.getDescription());
        task.setDeadline(updatedTask.getDeadline());
        task.setSubject(updatedTask.getSubject());
        return taskRepository.save(task);
    }

    public Task toggleTaskCompleted(Long taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));
        task.setCompleted(!task.isCompleted());
        return taskRepository.save(task);
    }

    @Transactional
    public void deleteTask(Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        List<Notification> related = notificationRepository
                .findByUserIdOrderByCreatedAtDesc(task.getUser().getId())
                .stream()
                .filter(n -> n.getMessage().contains("\"" + task.getTitle() + "\""))
                .collect(Collectors.toList());
        notificationRepository.deleteAll(related);

        taskRepository.deleteById(id);
    }
}