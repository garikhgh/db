package com.griddynamics.casandra.repository;

import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;

import com.griddynamics.casandra.model.ActivityLog;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

//@Repository
public interface ActivityLogRepository extends CassandraRepository<ActivityLog, UUID> {

    @Query("SELECT * FROM activity_log WHERE user_id = ?0")
    List<ActivityLog> findByUserId(UUID userId);

    @Query("SELECT * FROM activity_log WHERE user_id = ?0 AND timestamp >= ?1 AND timestamp <= ?2")
    List<ActivityLog> findByUserIdAndTimeRange(UUID userId, Instant from, Instant to);

    @Query("SELECT * FROM activity_log WHERE user_id = ?0 LIMIT ?1")
    List<ActivityLog> findRecentByUserId(UUID userId, int limit);
}
