package com.griddynamics.casandra.dto;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
@Builder
public class ActivityLogDto {
    private UUID userId;
    private Instant timestamp;
    private UUID activityId;
    private String activityType;
}
