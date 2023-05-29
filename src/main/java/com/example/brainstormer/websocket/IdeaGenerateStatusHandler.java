package com.example.brainstormer.websocket;

import com.example.brainstormer.repository.TopicRepository;
import com.example.brainstormer.service.AuthenticationService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Future;

@Component
public class IdeaGenerateStatusHandler extends TopicRelatedHandler {

    public static final String READY = "READY";
    public static final String PENDING = "PENDING";
    private final Map<UUID, Future<String>> generationTasks = new HashMap<>();

    public IdeaGenerateStatusHandler(AuthenticationService authenticationService, TopicRepository topicRepository) {
        super(authenticationService, topicRepository);
    }

    @Override
    protected void handleTextMessage(@NonNull WebSocketSession session, @NonNull TextMessage message) throws Exception {
        super.handleTextMessage(session, message);
        sendStatusMessage(session);
    }

    private void sendStatusMessage(@NonNull WebSocketSession session) throws IOException {
        UUID topicId = (UUID) session.getAttributes().get(TOPIC_ID);
        if (containsTask(topicId)) {
            session.sendMessage(responseMessageOf(PENDING));
        } else {
            session.sendMessage(responseMessageOf(READY));
        }
    }

    public void addGenerationTask(UUID topicId, Future<String> task) {
        generationTasks.put(topicId, task);
        notifyAboutNewStatus(topicId, PENDING);
    }

    public void removeCompletedTask(UUID topicId) {
        generationTasks.remove(topicId);
        notifyAboutNewStatus(topicId, READY);
    }

    public boolean containsTask(UUID topicId) {
        return generationTasks.containsKey(topicId);
    }

    private void notifyAboutNewStatus(UUID topicId, String status) {
        for (WebSocketSession session : sessions) {
            if (shouldSendMessage(session, topicId)) {
                try {
                    session.sendMessage(responseMessageOf(status));
                } catch (IOException ignored) {
                }
            }
        }
    }

    private boolean shouldSendMessage(WebSocketSession session, UUID topicId) {
        return session.isOpen() && topicId.equals(session.getAttributes().get(TOPIC_ID));
    }

    private WebSocketMessage<String> responseMessageOf(String status) {
        try {
            String value = objectMapper.writeValueAsString(new StatusResponse(status));
            return new TextMessage(value);
        } catch (JsonProcessingException e) {
            return new TextMessage("{\"status\":\"ERROR\"}");
        }
    }

    private record StatusResponse(String status) {
    }

}
