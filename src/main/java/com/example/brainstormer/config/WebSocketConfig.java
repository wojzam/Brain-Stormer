package com.example.brainstormer.config;

import com.example.brainstormer.websocket.IdeaGenerateStatusHandler;
import com.example.brainstormer.websocket.IdeaUpdatesHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketConfigurer {

    private final IdeaUpdatesHandler ideaUpdatesHandler;
    private final IdeaGenerateStatusHandler ideaGenerateStatusHandler;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(ideaUpdatesHandler, "/ws/idea-updates")
                .setAllowedOrigins("*");

        registry.addHandler(ideaGenerateStatusHandler, "/ws/generate-status")
                .setAllowedOrigins("*");
    }
}
