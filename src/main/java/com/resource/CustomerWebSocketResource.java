package com.resource;

import com.dto.CustomerDTO;
import com.dto.CustomerRequestMessage;
import com.service.CustomerWebSocketService;
import io.quarkus.websockets.next.*;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;

import java.util.List;


@Slf4j
@WebSocket(path = "/customers")
public class CustomerWebSocketResource {
    private static final String LAST_ACTIVE = "lastActive";

    @Inject
    CustomerWebSocketService socketService;

    @OnOpen
    void onOpen(WebSocketConnection connection) {
        connection.userData().put(UserData.TypedKey.forLong(LAST_ACTIVE), System.currentTimeMillis());
        log.info("Open connection: " + connection.id());
    }

    @OnTextMessage
    public Uni<List<CustomerDTO>> onMessage(WebSocketConnection connection, CustomerRequestMessage message) {
        connection.userData().put(UserData.TypedKey.forLong(LAST_ACTIVE), System.currentTimeMillis());
        return switch (message.getMessageType()) {
            case CREATE -> socketService.create(message);
            case READ -> socketService.findById(message.getCustomer().getId());
            case UPDATE -> socketService.update(message);
            case DELETE -> socketService.delete(message.getCustomer().getId());
            case READ_ALL -> socketService.list();
            default -> Uni.createFrom().failure(new UnsupportedOperationException("Unknown action"));
        };
    }
}
