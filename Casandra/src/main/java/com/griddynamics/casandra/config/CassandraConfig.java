package com.griddynamics.casandra.config;

import com.datastax.oss.driver.api.core.CqlSession;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.cassandra.core.cql.CqlTemplate;

import java.net.InetSocketAddress;

@Configuration
public class CassandraConfig {
    @Bean
    public CqlSession cassandraSession() {
        return CqlSession.builder()
                .addContactPoint(new InetSocketAddress("127.0.0.1", 9042))
                .withLocalDatacenter("datacenter1")
                .withKeyspace("user_logs")
                .build();
    }
    @Bean
    public CqlTemplate cqlTemplate(CqlSession cassandraSession) {
        return new CqlTemplate(cassandraSession);
    }
}
