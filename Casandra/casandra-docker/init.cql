CREATE KEYSPACE IF NOT EXISTS user_logs
    WITH replication = {'class': 'SimpleStrategy',
        'replication_factor': 3};

CREATE TABLE IF NOT EXISTS user_logs.activity_log (
    user_id UUID,
    activity_id UUID,
    activity_type TEXT,
    timestamp TIMESTAMP,
    PRIMARY KEY (user_id, timestamp)
) WITH CLUSTERING ORDER BY (timestamp DESC)
   AND default_time_to_live = 2592000; -- 30 days in seconds