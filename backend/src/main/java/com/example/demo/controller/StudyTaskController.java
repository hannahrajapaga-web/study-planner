package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.example.demo.model.StudyTask;
import com.example.demo.service.StudyTaskService;

@RestController
@RequestMapping("/tasks")
@CrossOrigin
public class StudyTaskController {

    @Autowired
    private StudyTaskService service;

    @GetMapping("/{userId}")
    public List<StudyTask> getTasksByUser(@PathVariable Long userId){
        return service.getTasksByUser(userId);
    }

    @PostMapping
    public StudyTask addTask(@RequestBody StudyTask task){
        return service.addTask(task);
    }

    @DeleteMapping("/{id}")
    public void deleteTask(@PathVariable Long id){
        service.deleteTask(id);
    }

    @PutMapping("/{id}/complete")
    public StudyTask markComplete(@PathVariable Long id){
        return service.markComplete(id);
    }
}