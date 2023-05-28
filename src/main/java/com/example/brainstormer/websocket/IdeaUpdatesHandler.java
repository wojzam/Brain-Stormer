package com.example.brainstormer.websocket;

import com.example.brainstormer.dto.IdeaUpdateMessage;
import com.example.brainstormer.model.Topic;
import com.example.brainstormer.model.User;
import com.example.brainstormer.repository.TopicRepository;
import com.example.brainstormer.service.AuthenticationService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class IdeaUpdatesHandler extends TextWebSocketHandler {

    private static final String TOKEN = "token";
    private static final String TOPIC_ID = "topicId";
    private static final String USER_ID = "userId";
    private final Logger logger = LoggerFactory.getLogger(IdeaUpdatesHandler.class);
    private final Set<WebSocketSession> sessions = new HashSet<>();
    private final AuthenticationService authenticationService;
    private final TopicRepository topicRepository;
    private final ObjectMapper objectMapper;

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

    @KafkaListener(topics = "idea-updates", groupId = "groupId")
    public void receiveIdeaUpdate(IdeaUpdateMessage ideaUpdateMessage) throws IOException {
        String ideaUpdateJson = objectMapper.writeValueAsString(ideaUpdateMessage);
        for (WebSocketSession session : sessions) {
            if (shouldSendMessage(session, ideaUpdateMessage)) {
                session.sendMessage(new TextMessage(ideaUpdateJson));
            }
        }
    }

    private boolean shouldSendMessage(WebSocketSession session, IdeaUpdateMessage ideaUpdateMessage) {
        return session.isOpen() &&
                ideaUpdateMessage.getTopicId().equals(session.getAttributes().get(TOPIC_ID)) &&
                !ideaUpdateMessage.getUserId().equals(session.getAttributes().get(USER_ID));
    }
}
