package com.example.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.StudyTask;
import com.example.demo.repository.StudyTaskRepository;

@Service
public class StudyTaskService {

    @Autowired
    private StudyTaskRepository repository;

    public List<StudyTask> getAllTasks() {
        return repository.findAll();
    }

    public StudyTask addTask(StudyTask task) {
        System.out.println("USER ID RECEIVED: " + task.getUserId()); // 👈 ADD THIS
        return repository.save(task);
    }

    public void deleteTask(Long id) {
        repository.deleteById(id);
    }

    public StudyTask markComplete(Long id) {

        StudyTask task = repository.findById(id).orElse(null);

        if(task != null){
            task.setCompleted(true);
            return repository.save(task);
        }

        return null;
    }

    public List<StudyTask> getTasksByUser(Long userId) {
        return repository.findByUserId(userId);
    }}