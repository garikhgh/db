package com.griddynamics.casandra.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import com.griddynamics.casandra.dto.ActivityLogDto;
import com.griddynamics.casandra.model.ActivityLog;
import com.griddynamics.casandra.repository.ActivityLogPersistence;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ActivityLogService {

    private final ActivityLogPersistence activityLogPersistence;


    public Boolean logActivity(UUID userId, String activityType) {
        return activityLogPersistence.saveLogActivityWithQuorum(userId, activityType);
    }

    public List<ActivityLogDto> getUserActivities(UUID userId) {
        return activityLogPersistence.findByUserIdWithConsistency(userId);
    }

    public List<ActivityLogDto> getUserActivitiesInRange(UUID userId, Instant from, Instant to) {
        return activityLogPersistence.findByUserIdAndTimeRange(userId, from, to);
    }

    public List<ActivityLogDto> getRecentActivities(UUID userId, int limit) {
        return activityLogPersistence.findRecentByUserId(userId, limit);
    }
}
