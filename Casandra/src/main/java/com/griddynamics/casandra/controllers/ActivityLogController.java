package com.griddynamics.casandra.controllers;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.griddynamics.casandra.dto.ActivityLogDto;
import com.griddynamics.casandra.services.ActivityLogService;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Slf4j
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
    public ResponseEntity<List<ActivityLogDto>> getAllActivities(@PathVariable UUID userId) {
        final List<ActivityLogDto> userActivities = service.getUserActivities(userId);
        log.info("User activity logs {}", userActivities);
        return ResponseEntity.ok(service.getUserActivities(userId));
    }

    @GetMapping("/user/{userId}/range")
    public ResponseEntity<List<ActivityLogDto>> getActivitiesInRange(
            @PathVariable UUID userId,
            @RequestParam Instant from,
            @RequestParam Instant to) {
        final List<ActivityLogDto> userActivitiesInRange = service.getUserActivitiesInRange(userId, from, to);
        log.info("User activity logs in given range from {} to {}: {}", from, to, userActivitiesInRange);
        return ResponseEntity.ok(service.getUserActivitiesInRange(userId, from, to));
    }

    @GetMapping("/user/{userId}/recent")
    public ResponseEntity<List<ActivityLogDto>> getRecentActivities(
            @PathVariable UUID userId,
            @RequestParam(defaultValue = "10") int limit) {
        final List<ActivityLogDto> recentActivities = service.getRecentActivities(userId, limit);
        log.info("User activity logs with given limits {}: {}", limit, recentActivities);
        return ResponseEntity.ok(service.getRecentActivities(userId, limit));
    }
}
