package com.example.studyplanner.controller;

import com.example.studyplanner.model.StudySchedule;
import com.example.studyplanner.model.User;
import com.example.studyplanner.service.StudyScheduleService;
import com.example.studyplanner.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/schedules")
@CrossOrigin(origins = {"http://localhost:5173", "https://hannahstudyplanner.vercel.app"})
public class StudyScheduleController {

    @Autowired
    private StudyScheduleService studyScheduleService;

    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<List<StudySchedule>> getSchedules(Principal principal) {
        User user = userService.findByUsername(principal.getName()).get();
        return ResponseEntity.ok(studyScheduleService.getSchedulesByUser(user.getId()));
    }

    @PostMapping
    public ResponseEntity<StudySchedule> createSchedule(@RequestBody StudySchedule schedule, Principal principal) {
        User user = userService.findByUsername(principal.getName()).get();
        schedule.setUser(user);
        return ResponseEntity.ok(studyScheduleService.createSchedule(schedule));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteSchedule(@PathVariable Long id) {
        studyScheduleService.deleteSchedule(id);
        return ResponseEntity.ok().build();
    }
}