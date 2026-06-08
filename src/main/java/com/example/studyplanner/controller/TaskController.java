package com.example.studyplanner.controller;

import com.example.studyplanner.model.Task;
import com.example.studyplanner.model.User;
import com.example.studyplanner.service.TaskService;
import com.example.studyplanner.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/tasks")
@CrossOrigin(origins = {"http://localhost:5173", "https://hannahstudyplanner.vercel.app"})
public class TaskController {

    @Autowired
    private TaskService taskService;

    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<List<Task>> getTasks(Principal principal) {
        User user = userService.findByUsername(principal.getName()).get();
        return ResponseEntity.ok(taskService.getTasksByUser(user.getId()));
    }

    @PostMapping
    public ResponseEntity<Task> createTask(@RequestBody Task task, Principal principal) {
        User user = userService.findByUsername(principal.getName()).get();
        task.setUser(user);
        return ResponseEntity.ok(taskService.createTask(task));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Task> updateTask(@PathVariable Long id, @RequestBody Task updatedTask) {
        return ResponseEntity.ok(taskService.updateTask(id, updatedTask));
    }

    @PutMapping("/{id}/toggle")
    public ResponseEntity<Task> toggleTask(@PathVariable Long id) {
        return ResponseEntity.ok(taskService.toggleTaskCompleted(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
        return ResponseEntity.ok().build();
    }
}