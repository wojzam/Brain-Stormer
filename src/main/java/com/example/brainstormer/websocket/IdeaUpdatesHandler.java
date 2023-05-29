package com.example.brainstormer.websocket;

import com.example.brainstormer.dto.IdeaUpdateMessage;
import com.example.brainstormer.repository.TopicRepository;
import com.example.brainstormer.service.AuthenticationService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;

@Component
public class IdeaUpdatesHandler extends TopicRelatedHandler {

    public IdeaUpdatesHandler(AuthenticationService authenticationService, TopicRepository topicRepository) {
        super(authenticationService, topicRepository);
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
