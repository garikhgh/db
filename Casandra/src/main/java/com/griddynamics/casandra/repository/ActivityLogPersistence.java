package com.griddynamics.casandra.repository;

import com.datastax.oss.driver.api.core.ConsistencyLevel;
import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.Row;
import com.datastax.oss.driver.api.core.cql.SimpleStatement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.cassandra.core.CassandraOperations;
import org.springframework.data.cassandra.core.cql.CqlTemplate;
import org.springframework.stereotype.Service;

import com.griddynamics.casandra.model.ActivityLog;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
@Slf4j
@Service
@RequiredArgsConstructor
public class ActivityLogPersistence {

    private final CassandraOperations cassandraOperations;
    private final CqlTemplate cqlTemplate;
    private final CqlSession session;


    public boolean saveLogActivityWithQuorum(UUID userId, String activityType) {
        UUID activityId = UUID.randomUUID();
        Instant timestamp = Instant.now();
        try {
            SimpleStatement stmt = SimpleStatement.builder(
                            "INSERT INTO activity_log (user_id, activity_id, activity_type, timestamp) VALUES (?, ?, ?, ?)")
                    .addPositionalValues(userId, activityId, activityType, timestamp)
                    .setConsistencyLevel(ConsistencyLevel.LOCAL_QUORUM)
                    .build();
            session.execute(stmt);
            return true;
        } catch (Exception e) {
            log.error("Error while saving activity log", e);
            return false;
        }

    }

    public List<ActivityLog> findByUserIdWithConsistency(UUID userId) {
        SimpleStatement stmt = SimpleStatement.builder("SELECT * FROM activity_log WHERE user_id = ?")
                .addPositionalValue(userId)
                .setConsistencyLevel(ConsistencyLevel.LOCAL_QUORUM)
                .build();

        return cqlTemplate.query(stmt, (row, rowNum)-> buildActivityLog(row));
    }

    public List<ActivityLog> findByUserIdAndTimeRange(UUID userId, Instant from, Instant to) {

        SimpleStatement stmt = SimpleStatement.builder(
                        "SELECT * FROM activity_log WHERE user_id = ? AND timestamp >= ? AND timestamp <= ?")
                .addPositionalValue(userId)
                .addPositionalValue(from)
                .addPositionalValue(to)
                .setConsistencyLevel(ConsistencyLevel.LOCAL_QUORUM)
                .build();

        return cqlTemplate.query(stmt, (row, rowNum) -> buildActivityLog(row));
    }

    public List<ActivityLog> findRecentByUserId(UUID userId, int limit){
        SimpleStatement stmt = SimpleStatement.builder(
                        "SELECT * FROM activity_log WHERE user_id = ? LIMIT ?")
                .addPositionalValue(userId)
                .addPositionalValue(limit)
                .setConsistencyLevel(ConsistencyLevel.LOCAL_QUORUM)
                .build();

        return cqlTemplate.query(stmt, (row, rowNum) -> buildActivityLog(row));
    }

    private ActivityLog buildActivityLog(Row row) {
        ActivityLog log = new ActivityLog();
        log.setUserId(row.getUuid("user_id"));
        log.setActivity_Id(row.getUuid("activity_id"));
        log.setActivity_Type(row.getString("activity_Type"));
        log.setTimestamp(row.getInstant("timestamp"));
        return log;
    }
}
