package com.example.brainstormer.websocket;

import com.example.brainstormer.model.Topic;
import com.example.brainstormer.model.User;
import com.example.brainstormer.repository.TopicRepository;
import com.example.brainstormer.service.AuthenticationService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public abstract class TopicRelatedHandler extends TextWebSocketHandler {

    protected static final String TOKEN = "token";
    protected static final String TOPIC_ID = "topicId";
    protected static final String USER_ID = "userId";
    protected final Logger logger = LoggerFactory.getLogger(TopicRelatedHandler.class);
    protected final Set<WebSocketSession> sessions = new HashSet<>();
    protected final AuthenticationService authenticationService;
    protected final TopicRepository topicRepository;
    protected final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void afterConnectionEstablished(@NonNull WebSocketSession session) {
        logger.info("New websocket session " + session.getId());
        sessions.add(session);
    }

    @Override
    public void afterConnectionClosed(@NonNull WebSocketSession session, CloseStatus status) {
        logger.info("Session closed with status " + status.getCode());
        sessions.remove(session);
    }

    @Override
    protected void handleTextMessage(@NonNull WebSocketSession session, @NonNull TextMessage message) throws Exception {
        try {
            Map<String, String> json = objectMapper.readValue(message.getPayload(), new TypeReference<>() {
            });
            Topic topic = topicRepository.findById(UUID.fromString(json.get(TOPIC_ID))).orElseThrow();
            UUID userId = getUserId(json.get(TOKEN), topic);

            session.getAttributes().put(TOPIC_ID, topic.getId());
            session.getAttributes().put(USER_ID, userId);

            logger.info("Session " + session.getId() + " authorized");
        } catch (Exception ignored) {
            logger.warn("Session rejected");
            session.close();
        }
    }

    private UUID getUserId(String token, Topic topic) throws RuntimeException {
        User user;
        try {
            user = authenticationService.getUserFromToken(token);
        } catch (Exception ignored) {
            if (topic.isPublicVisibility()) {
                return UUID.randomUUID();
            }
            throw new RuntimeException();
        }

        if (!topic.isPublicVisibility() && topic.userAccessDenied(user)) {
            throw new RuntimeException();
        }
        return user.getId();
    }
}
