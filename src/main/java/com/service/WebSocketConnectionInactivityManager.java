package com.service;

import io.quarkus.scheduler.Scheduled;
import io.quarkus.websockets.next.OpenConnections;
import io.quarkus.websockets.next.UserData;
import io.quarkus.websockets.next.WebSocketConnection;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.time.Duration;
import java.time.Instant;

@Slf4j
@ApplicationScoped
public class WebSocketConnectionInactivityManager {

    private static final String LAST_ACTIVE = "lastActive";

    @ConfigProperty(name = "scheduler.socket-inactive.max-period")
    Duration maxInactivity;

    @Inject
    OpenConnections connections;

    @Scheduled(every = "${scheduler.socket-inspection.interval}")
    void closeInactiveConnections() {
        connections.stream().filter(this::isExpired).forEach(c -> {
            c.closeAndAwait();
            log.info("Closed inactive connection: " + c.id());
        });

    }

    private boolean isExpired(WebSocketConnection connection) {
        Long lastActive = connection.userData().get(UserData.TypedKey.forLong(LAST_ACTIVE));

        if (lastActive == null) return false;

        return Instant.now().isAfter(Instant.ofEpochMilli(lastActive).plus(maxInactivity));
    }
}
