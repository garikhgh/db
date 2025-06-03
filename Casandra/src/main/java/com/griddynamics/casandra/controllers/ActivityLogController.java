package com.griddynamics.casandra.controllers;


import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.griddynamics.casandra.model.ActivityLog;
import com.griddynamics.casandra.services.ActivityLogService;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/activity")
@RequiredArgsConstructor
public class ActivityLogController {

    private final ActivityLogService service;

    @PostMapping("/log")
    public ResponseEntity<Void> logActivity(@RequestParam UUID userId, @RequestParam String activityType) {
        final Boolean isSaved = service.logActivity(userId, activityType);
        if (isSaved) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ActivityLog>> getAllActivities(@PathVariable UUID userId) {
        return ResponseEntity.ok(service.getUserActivities(userId));
    }

    @GetMapping("/user/{userId}/range")
    public ResponseEntity<List<ActivityLog>> getActivitiesInRange(
            @PathVariable UUID userId,
            @RequestParam Instant from,
            @RequestParam Instant to) {
        return ResponseEntity.ok(service.getUserActivitiesInRange(userId, from, to));
    }

    @GetMapping("/user/{userId}/recent")
    public ResponseEntity<List<ActivityLog>> getRecentActivities(
            @PathVariable UUID userId,
            @RequestParam(defaultValue = "10") int limit) {
        return ResponseEntity.ok(service.getRecentActivities(userId, limit));
    }
}
