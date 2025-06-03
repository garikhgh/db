package com.griddynamics.casandra.model;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import org.springframework.data.cassandra.core.cql.Ordering;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyClass;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;

import java.time.Instant;
import java.util.UUID;

@Data
@Table("activity_log")
@Builder
@ToString
public class ActivityLog {

    @PrimaryKeyClass
    @Builder
    @ToString
    public static class ActivityLogKey {
        @PrimaryKeyColumn(name = "user_id", ordinal = 0, type = PrimaryKeyType.PARTITIONED)
        private UUID userId;

        @PrimaryKeyColumn(name = "timestamp", ordinal = 1, type = PrimaryKeyType.CLUSTERED, ordering = Ordering.DESCENDING)
        private Instant timestamp;

        @PrimaryKeyColumn(name = "activity_id", ordinal = 2, type = PrimaryKeyType.CLUSTERED)
        private UUID activityId;
    }

    @PrimaryKey
    private ActivityLogKey key;

    @Column("activity_type")
    private String activityType;
}
